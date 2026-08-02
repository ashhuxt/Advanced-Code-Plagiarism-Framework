package com.plagiarism.parser.adapter;

import com.plagiarism.parser.api.CodeParser;
import com.plagiarism.parser.api.ParseException;
import com.plagiarism.parser.model.ParsedCodeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Adapter implementation for parsing Python source code.
 * Uses regex-based parsing for structural component extraction.
 */
public class PythonParserAdapter implements CodeParser {

    // Patterns for Python syntax elements
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "^\\s*def\\s+(\\w+)\\s*\\(", Pattern.MULTILINE);
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "^\\s*class\\s+(\\w+)", Pattern.MULTILINE);
    private static final Pattern FOR_LOOP_PATTERN = Pattern.compile(
        "^\\s*for\\s+.+\\s+in\\s+.+:\\s*$", Pattern.MULTILINE);
    private static final Pattern WHILE_LOOP_PATTERN = Pattern.compile(
        "^\\s*while\\s+.+:\\s*$", Pattern.MULTILINE);
    private static final Pattern IF_PATTERN = Pattern.compile(
        "^\\s*if\\s+.+:\\s*$", Pattern.MULTILINE);
    private static final Pattern ELIF_PATTERN = Pattern.compile(
        "^\\s*elif\\s+.+:\\s*$", Pattern.MULTILINE);
    private static final Pattern ELSE_PATTERN = Pattern.compile(
        "^\\s*else\\s*:\\s*$", Pattern.MULTILINE);
    private static final Pattern VARIABLE_PATTERN = Pattern.compile(
        "^\\s*(\\w+)\\s*=\\s*.+", Pattern.MULTILINE);
    private static final Pattern IMPORT_PATTERN = Pattern.compile(
        "^\\s*(?:from|import)\\s+.+", Pattern.MULTILINE);

    @Override
    public List<ParsedCodeUnit> parse(String sourceCode) throws ParseException {
        try {
            List<ParsedCodeUnit> units = new ArrayList<>();
            String[] lines = sourceCode.split("\n");

            // Extract functions
            Matcher funcMatcher = FUNCTION_PATTERN.matcher(sourceCode);
            while (funcMatcher.find()) {
                int lineNum = getLineNumber(sourceCode, funcMatcher.start());
                ParsedCodeUnit unit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.FUNCTION)
                    .name(funcMatcher.group(1))
                    .content(extractFunctionBody(lines, lineNum))
                    .startLine(lineNum)
                    .endLine(findFunctionEnd(lines, lineNum))
                    .build();
                units.add(unit);
            }

            // Extract classes
            Matcher classMatcher = CLASS_PATTERN.matcher(sourceCode);
            while (classMatcher.find()) {
                int lineNum = getLineNumber(sourceCode, classMatcher.start());
                ParsedCodeUnit unit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.CLASS)
                    .name(classMatcher.group(1))
                    .content(extractClassBody(lines, lineNum))
                    .startLine(lineNum)
                    .endLine(findClassEnd(lines, lineNum))
                    .build();
                units.add(unit);
            }

            // Extract loops
            Matcher forMatcher = FOR_LOOP_PATTERN.matcher(sourceCode);
            while (forMatcher.find()) {
                int lineNum = getLineNumber(sourceCode, forMatcher.start());
                String content = forMatcher.group();
                ParsedCodeUnit unit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.LOOP)
                    .name("for")
                    .content(content)
                    .startLine(lineNum)
                    .endLine(lineNum)
                    .build();
                unit.addMetadata("loopType:for");
                units.add(unit);
            }

            Matcher whileMatcher = WHILE_LOOP_PATTERN.matcher(sourceCode);
            while (whileMatcher.find()) {
                int lineNum = getLineNumber(sourceCode, whileMatcher.start());
                String content = whileMatcher.group();
                ParsedCodeUnit unit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.LOOP)
                    .name("while")
                    .content(content)
                    .startLine(lineNum)
                    .endLine(lineNum)
                    .build();
                unit.addMetadata("loopType:while");
                units.add(unit);
            }

            // Extract conditions
            extractConditions(sourceCode, units, IF_PATTERN, "if");
            extractConditions(sourceCode, units, ELIF_PATTERN, "elif");
            extractConditions(sourceCode, units, ELSE_PATTERN, "else");

            // Extract variables (simple assignments at module/class level)
            Matcher varMatcher = VARIABLE_PATTERN.matcher(sourceCode);
            while (varMatcher.find()) {
                String varName = varMatcher.group(1);
                // Skip if it looks like a function parameter or loop variable
                if (!varName.equals("i") && !varName.equals("j") && !varName.equals("k")) {
                    int lineNum = getLineNumber(sourceCode, varMatcher.start());
                    ParsedCodeUnit unit = ParsedCodeUnit.builder()
                        .type(ParsedCodeUnit.UnitType.VARIABLE)
                        .name(varName)
                        .content(varMatcher.group())
                        .startLine(lineNum)
                        .endLine(lineNum)
                        .build();
                    units.add(unit);
                }
            }

            return units;
        } catch (Exception e) {
            throw new ParseException("Error parsing Python code", e);
        }
    }

    @Override
    public String getSupportedLanguage() {
        return "Python";
    }

    @Override
    public boolean supportsExtension(String fileExtension) {
        return ".py".equalsIgnoreCase(fileExtension);
    }

    @Override
    public List<String> extractFunctions(String sourceCode) throws ParseException {
        List<String> functions = new ArrayList<>();
        Matcher matcher = FUNCTION_PATTERN.matcher(sourceCode);
        while (matcher.find()) {
            functions.add(matcher.group(1));
        }
        return functions;
    }

    @Override
    public List<String> extractLoops(String sourceCode) throws ParseException {
        List<String> loops = new ArrayList<>();
        
        Matcher forMatcher = FOR_LOOP_PATTERN.matcher(sourceCode);
        while (forMatcher.find()) {
            loops.add(forMatcher.group());
        }
        
        Matcher whileMatcher = WHILE_LOOP_PATTERN.matcher(sourceCode);
        while (whileMatcher.find()) {
            loops.add(whileMatcher.group());
        }
        
        return loops;
    }

    @Override
    public List<String> extractConditions(String sourceCode) throws ParseException {
        List<String> conditions = new ArrayList<>();
        
        Matcher ifMatcher = IF_PATTERN.matcher(sourceCode);
        while (ifMatcher.find()) {
            conditions.add(ifMatcher.group());
        }
        
        Matcher elifMatcher = ELIF_PATTERN.matcher(sourceCode);
        while (elifMatcher.find()) {
            conditions.add(elifMatcher.group());
        }
        
        Matcher elseMatcher = ELSE_PATTERN.matcher(sourceCode);
        while (elseMatcher.find()) {
            conditions.add(elseMatcher.group());
        }
        
        return conditions;
    }

    @Override
    public List<String> extractVariables(String sourceCode) throws ParseException {
        List<String> variables = new ArrayList<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(sourceCode);
        while (matcher.find()) {
            String varName = matcher.group(1);
            if (!variables.contains(varName)) {
                variables.add(varName);
            }
        }
        return variables;
    }

    private void extractConditions(String sourceCode, List<ParsedCodeUnit> units, 
                                   Pattern pattern, String conditionType) {
        Matcher matcher = pattern.matcher(sourceCode);
        while (matcher.find()) {
            int lineNum = getLineNumber(sourceCode, matcher.start());
            String content = matcher.group();
            ParsedCodeUnit unit = ParsedCodeUnit.builder()
                .type(ParsedCodeUnit.UnitType.CONDITION)
                .name(conditionType)
                .content(content)
                .startLine(lineNum)
                .endLine(lineNum)
                .build();
            unit.addMetadata("conditionType:" + conditionType);
            units.add(unit);
        }
    }

    private int getLineNumber(String sourceCode, int charIndex) {
        int lineNum = 1;
        for (int i = 0; i < charIndex && i < sourceCode.length(); i++) {
            if (sourceCode.charAt(i) == '\n') {
                lineNum++;
            }
        }
        return lineNum;
    }

    private String extractFunctionBody(String[] lines, int startLine) {
        StringBuilder body = new StringBuilder();
        int indent = -1;
        
        for (int i = startLine - 1; i < lines.length; i++) {
            String line = lines[i];
            if (i == startLine - 1) {
                body.append(line).append("\n");
                // Calculate base indentation
                int spaces = countLeadingSpaces(line);
                indent = spaces + 4; // Function body should be indented more
            } else if (line.trim().isEmpty()) {
                body.append(line).append("\n");
            } else {
                int spaces = countLeadingSpaces(line);
                if (spaces >= indent) {
                    body.append(line).append("\n");
                } else {
                    break;
                }
            }
        }
        return body.toString();
    }

    private String extractClassBody(String[] lines, int startLine) {
        StringBuilder body = new StringBuilder();
        int indent = -1;
        
        for (int i = startLine - 1; i < lines.length; i++) {
            String line = lines[i];
            if (i == startLine - 1) {
                body.append(line).append("\n");
                int spaces = countLeadingSpaces(line);
                indent = spaces + 4;
            } else if (line.trim().isEmpty()) {
                body.append(line).append("\n");
            } else {
                int spaces = countLeadingSpaces(line);
                if (spaces >= indent) {
                    body.append(line).append("\n");
                } else {
                    break;
                }
            }
        }
        return body.toString();
    }

    private int findFunctionEnd(String[] lines, int startLine) {
        int indent = -1;
        int endLine = startLine;
        
        for (int i = startLine - 1; i < lines.length; i++) {
            String line = lines[i];
            if (i == startLine - 1) {
                int spaces = countLeadingSpaces(line);
                indent = spaces + 4;
                endLine = i + 1;
            } else if (!line.trim().isEmpty()) {
                int spaces = countLeadingSpaces(line);
                if (spaces >= indent) {
                    endLine = i + 1;
                } else {
                    break;
                }
            }
        }
        return endLine;
    }

    private int findClassEnd(String[] lines, int startLine) {
        int indent = -1;
        int endLine = startLine;
        
        for (int i = startLine - 1; i < lines.length; i++) {
            String line = lines[i];
            if (i == startLine - 1) {
                int spaces = countLeadingSpaces(line);
                indent = spaces + 4;
                endLine = i + 1;
            } else if (!line.trim().isEmpty()) {
                int spaces = countLeadingSpaces(line);
                if (spaces >= indent) {
                    endLine = i + 1;
                } else {
                    break;
                }
            }
        }
        return endLine;
    }

    private int countLeadingSpaces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                count++;
            } else if (c == '\t') {
                count += 4; // Treat tab as 4 spaces
            } else {
                break;
            }
        }
        return count;
    }
}

package com.plagiarism.parser.adapter;

import com.plagiarism.parser.api.CodeParser;
import com.plagiarism.parser.api.ParseException;
import com.plagiarism.parser.model.ParsedCodeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapter implementation for parsing JavaScript source code.
 * Uses regex-based parsing for structural component extraction.
 */
public class JSParserAdapter implements CodeParser {

    // Patterns for JavaScript syntax elements
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
        "(?:function\\s+(\\w+)|(?:const|let|var)\\s+(\\w+)\\s*=\\s*(?:async\\s+)?(?:\\([^)]*\\)|[^=])\\s*=>)", 
        Pattern.MULTILINE);
    private static final Pattern ARROW_FUNCTION_PATTERN = Pattern.compile(
        "(?:const|let|var)\\s+(\\w+)\\s*=\\s*(?:async\\s+)?(?:\\([^)]*\\)|[^=])\\s*=>", 
        Pattern.MULTILINE);
    private static final Pattern CLASS_PATTERN = Pattern.compile(
        "class\\s+(\\w+)", Pattern.MULTILINE);
    private static final Pattern FOR_LOOP_PATTERN = Pattern.compile(
        "for\\s*\\([^)]*\\)\\s*\\{?", Pattern.MULTILINE);
    private static final Pattern WHILE_LOOP_PATTERN = Pattern.compile(
        "while\\s*\\([^)]*\\)\\s*\\{?", Pattern.MULTILINE);
    private static final Pattern DO_WHILE_PATTERN = Pattern.compile(
        "do\\s*\\{", Pattern.MULTILINE);
    private static final Pattern IF_PATTERN = Pattern.compile(
        "if\\s*\\([^)]*\\)\\s*\\{?", Pattern.MULTILINE);
    private static final Pattern SWITCH_PATTERN = Pattern.compile(
        "switch\\s*\\([^)]*\\)\\s*\\{?", Pattern.MULTILINE);
    private static final Pattern VARIABLE_PATTERN = Pattern.compile(
        "(?:const|let|var)\\s+(\\w+)\\s*=", Pattern.MULTILINE);

    @Override
    public List<ParsedCodeUnit> parse(String sourceCode) throws ParseException {
        try {
            List<ParsedCodeUnit> units = new ArrayList<>();
            String[] lines = sourceCode.split("\n");

            // Extract functions (traditional and arrow)
            Matcher funcMatcher = FUNCTION_PATTERN.matcher(sourceCode);
            while (funcMatcher.find()) {
                String funcName = funcMatcher.group(1) != null ? funcMatcher.group(1) : funcMatcher.group(2);
                if (funcName != null) {
                    int lineNum = getLineNumber(sourceCode, funcMatcher.start());
                    ParsedCodeUnit unit = ParsedCodeUnit.builder()
                        .type(ParsedCodeUnit.UnitType.FUNCTION)
                        .name(funcName)
                        .content(extractFunctionBody(sourceCode, funcMatcher.start()))
                        .startLine(lineNum)
                        .endLine(lineNum + 5) // Approximate end
                        .build();
                    units.add(unit);
                }
            }

            // Extract classes
            Matcher classMatcher = CLASS_PATTERN.matcher(sourceCode);
            while (classMatcher.find()) {
                int lineNum = getLineNumber(sourceCode, classMatcher.start());
                ParsedCodeUnit unit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.CLASS)
                    .name(classMatcher.group(1))
                    .content(extractClassBody(sourceCode, classMatcher.start()))
                    .startLine(lineNum)
                    .endLine(lineNum + 10) // Approximate end
                    .build();
                units.add(unit);
            }

            // Extract loops
            extractLoops(sourceCode, units, FOR_LOOP_PATTERN, "for");
            extractLoops(sourceCode, units, WHILE_LOOP_PATTERN, "while");
            extractLoops(sourceCode, units, DO_WHILE_PATTERN, "do-while");

            // Extract conditions
            extractConditions(sourceCode, units, IF_PATTERN, "if");
            extractConditions(sourceCode, units, SWITCH_PATTERN, "switch");

            // Extract variables
            Matcher varMatcher = VARIABLE_PATTERN.matcher(sourceCode);
            while (varMatcher.find()) {
                String varName = varMatcher.group(1);
                int lineNum = getLineNumber(sourceCode, varMatcher.start());
                ParsedCodeUnit unit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.VARIABLE)
                    .name(varName)
                    .content(varMatcher.group())
                    .startLine(lineNum)
                    .endLine(lineNum)
                    .build();
                
                // Determine variable type from declaration
                String fullMatch = varMatcher.group();
                if (fullMatch.startsWith("const")) {
                    unit.addMetadata("modifier:const");
                } else if (fullMatch.startsWith("let")) {
                    unit.addMetadata("modifier:let");
                } else if (fullMatch.startsWith("var")) {
                    unit.addMetadata("modifier:var");
                }
                
                units.add(unit);
            }

            return units;
        } catch (Exception e) {
            throw new ParseException("Error parsing JavaScript code", e);
        }
    }

    @Override
    public String getSupportedLanguage() {
        return "JavaScript";
    }

    @Override
    public boolean supportsExtension(String fileExtension) {
        return ".js".equalsIgnoreCase(fileExtension) || 
               ".jsx".equalsIgnoreCase(fileExtension) ||
               ".ts".equalsIgnoreCase(fileExtension) ||
               ".tsx".equalsIgnoreCase(fileExtension);
    }

    @Override
    public List<String> extractFunctions(String sourceCode) throws ParseException {
        List<String> functions = new ArrayList<>();
        Matcher matcher = FUNCTION_PATTERN.matcher(sourceCode);
        while (matcher.find()) {
            String funcName = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            if (funcName != null && !functions.contains(funcName)) {
                functions.add(funcName);
            }
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
        
        Matcher doMatcher = DO_WHILE_PATTERN.matcher(sourceCode);
        while (doMatcher.find()) {
            loops.add(doMatcher.group());
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
        
        Matcher switchMatcher = SWITCH_PATTERN.matcher(sourceCode);
        while (switchMatcher.find()) {
            conditions.add(switchMatcher.group());
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

    private void extractLoops(String sourceCode, List<ParsedCodeUnit> units, 
                              Pattern pattern, String loopType) {
        Matcher matcher = pattern.matcher(sourceCode);
        while (matcher.find()) {
            int lineNum = getLineNumber(sourceCode, matcher.start());
            String content = matcher.group();
            ParsedCodeUnit unit = ParsedCodeUnit.builder()
                .type(ParsedCodeUnit.UnitType.LOOP)
                .name(loopType)
                .content(content)
                .startLine(lineNum)
                .endLine(lineNum)
                .build();
            unit.addMetadata("loopType:" + loopType);
            units.add(unit);
        }
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

    private String extractFunctionBody(String sourceCode, int startIndex) {
        int braceCount = 0;
        int endIndex = startIndex;
        boolean foundOpenBrace = false;
        
        for (int i = startIndex; i < sourceCode.length(); i++) {
            char c = sourceCode.charAt(i);
            if (c == '{') {
                braceCount++;
                foundOpenBrace = true;
            } else if (c == '}') {
                braceCount--;
                if (foundOpenBrace && braceCount == 0) {
                    endIndex = i + 1;
                    break;
                }
            }
        }
        
        // If no braces found (arrow function with implicit return), estimate length
        if (!foundOpenBrace) {
            int nextSemicolon = sourceCode.indexOf(';', startIndex);
            if (nextSemicolon > 0) {
                endIndex = nextSemicolon + 1;
            } else {
                endIndex = Math.min(startIndex + 100, sourceCode.length());
            }
        }
        
        return sourceCode.substring(startIndex, endIndex);
    }

    private String extractClassBody(String sourceCode, int startIndex) {
        int braceCount = 0;
        int endIndex = startIndex;
        boolean foundOpenBrace = false;
        
        for (int i = startIndex; i < sourceCode.length(); i++) {
            char c = sourceCode.charAt(i);
            if (c == '{') {
                braceCount++;
                foundOpenBrace = true;
            } else if (c == '}') {
                braceCount--;
                if (foundOpenBrace && braceCount == 0) {
                    endIndex = i + 1;
                    break;
                }
            }
        }
        
        if (!foundOpenBrace) {
            endIndex = Math.min(startIndex + 200, sourceCode.length());
        }
        
        return sourceCode.substring(startIndex, endIndex);
    }
}

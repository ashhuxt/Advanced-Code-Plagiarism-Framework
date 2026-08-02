package com.plagiarism.parser.adapter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.plagiarism.parser.api.CodeParser;
import com.plagiarism.parser.api.ParseException;
import com.plagiarism.parser.model.ParsedCodeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter implementation for parsing Java source code using JavaParser library.
 */
public class JavaParserAdapter implements CodeParser {

    private final JavaParser javaParser;

    public JavaParserAdapter() {
        this.javaParser = new JavaParser();
    }

    @Override
    public List<ParsedCodeUnit> parse(String sourceCode) throws ParseException {
        try {
            ParseResult<CompilationUnit> result = javaParser.parse(sourceCode);
            
            if (!result.isSuccessful() || result.getResult().isEmpty()) {
                throw new ParseException("Failed to parse Java source code: " + 
                    result.getProblems().stream().map(Object::toString).collect(Collectors.joining(", ")));
            }

            CompilationUnit cu = result.getResult().get();
            List<ParsedCodeUnit> units = new ArrayList<>();

            // Extract methods/functions
            cu.findAll(MethodDeclaration.class).forEach(method -> {
                ParsedCodeUnit methodUnit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.METHOD)
                    .name(method.getNameAsString())
                    .content(method.toString())
                    .startLine(method.getBegin().map(pos -> pos.line).orElse(0))
                    .endLine(method.getEnd().map(pos -> pos.line).orElse(0))
                    .build();
                
                // Add parameters as metadata
                method.getParameters().forEach(param -> 
                    methodUnit.addMetadata("param:" + param.getType() + " " + param.getName()));
                
                units.add(methodUnit);
            });

            // Extract loops
            cu.findAll(ForStmt.class).forEach(stmt -> {
                units.add(createLoopUnit(stmt, "for"));
            });
            cu.findAll(WhileStmt.class).forEach(stmt -> {
                units.add(createLoopUnit(stmt, "while"));
            });
            cu.findAll(DoStmt.class).forEach(stmt -> {
                units.add(createLoopUnit(stmt, "do-while"));
            });

            // Extract conditions
            cu.findAll(IfStmt.class).forEach(stmt -> {
                units.add(createConditionUnit(stmt, "if"));
            });
            cu.findAll(SwitchStmt.class).forEach(stmt -> {
                units.add(createConditionUnit(stmt, "switch"));
            });

            // Extract variables
            cu.findAll(VariableDeclarator.class).forEach(var -> {
                ParsedCodeUnit varUnit = ParsedCodeUnit.builder()
                    .type(ParsedCodeUnit.UnitType.VARIABLE)
                    .name(var.getNameAsString())
                    .content(var.toString())
                    .startLine(var.getBegin().map(pos -> pos.line).orElse(0))
                    .endLine(var.getEnd().map(pos -> pos.line).orElse(0))
                    .build();
                varUnit.addMetadata("type:" + var.getType());
                units.add(varUnit);
            });

            return units;
        } catch (Exception e) {
            throw new ParseException("Error parsing Java code", e);
        }
    }

    @Override
    public String getSupportedLanguage() {
        return "Java";
    }

    @Override
    public boolean supportsExtension(String fileExtension) {
        return ".java".equalsIgnoreCase(fileExtension);
    }

    @Override
    public List<String> extractFunctions(String sourceCode) throws ParseException {
        return parse(sourceCode).stream()
            .filter(unit -> unit.getType() == ParsedCodeUnit.UnitType.METHOD)
            .map(ParsedCodeUnit::getName)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> extractLoops(String sourceCode) throws ParseException {
        return parse(sourceCode).stream()
            .filter(unit -> unit.getType() == ParsedCodeUnit.UnitType.LOOP)
            .map(ParsedCodeUnit::getContent)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> extractConditions(String sourceCode) throws ParseException {
        return parse(sourceCode).stream()
            .filter(unit -> unit.getType() == ParsedCodeUnit.UnitType.CONDITION)
            .map(ParsedCodeUnit::getContent)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> extractVariables(String sourceCode) throws ParseException {
        return parse(sourceCode).stream()
            .filter(unit -> unit.getType() == ParsedCodeUnit.UnitType.VARIABLE)
            .map(ParsedCodeUnit::getName)
            .collect(Collectors.toList());
    }

    private ParsedCodeUnit createLoopUnit(Object stmt, String loopType) {
        String content = stmt.toString();
        int startLine = 0;
        int endLine = 0;
        
        if (stmt instanceof ForStmt) {
            ForStmt s = (ForStmt) stmt;
            startLine = s.getBegin().map(pos -> pos.line).orElse(0);
            endLine = s.getEnd().map(pos -> pos.line).orElse(0);
        } else if (stmt instanceof WhileStmt) {
            WhileStmt s = (WhileStmt) stmt;
            startLine = s.getBegin().map(pos -> pos.line).orElse(0);
            endLine = s.getEnd().map(pos -> pos.line).orElse(0);
        } else if (stmt instanceof DoStmt) {
            DoStmt s = (DoStmt) stmt;
            startLine = s.getBegin().map(pos -> pos.line).orElse(0);
            endLine = s.getEnd().map(pos -> pos.line).orElse(0);
        }

        ParsedCodeUnit unit = ParsedCodeUnit.builder()
            .type(ParsedCodeUnit.UnitType.LOOP)
            .name(loopType)
            .content(content)
            .startLine(startLine)
            .endLine(endLine)
            .build();
        unit.addMetadata("loopType:" + loopType);
        return unit;
    }

    private ParsedCodeUnit createConditionUnit(Object stmt, String conditionType) {
        String content = stmt.toString();
        int startLine = 0;
        int endLine = 0;
        
        if (stmt instanceof IfStmt) {
            IfStmt s = (IfStmt) stmt;
            startLine = s.getBegin().map(pos -> pos.line).orElse(0);
            endLine = s.getEnd().map(pos -> pos.line).orElse(0);
        } else if (stmt instanceof SwitchStmt) {
            SwitchStmt s = (SwitchStmt) stmt;
            startLine = s.getBegin().map(pos -> pos.line).orElse(0);
            endLine = s.getEnd().map(pos -> pos.line).orElse(0);
        }

        ParsedCodeUnit unit = ParsedCodeUnit.builder()
            .type(ParsedCodeUnit.UnitType.CONDITION)
            .name(conditionType)
            .content(content)
            .startLine(startLine)
            .endLine(endLine)
            .build();
        unit.addMetadata("conditionType:" + conditionType);
        return unit;
    }
}

package com.plagiarism.detector.util;

// Add these imports to resolve your errors
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import java.util.HashMap;
import java.util.Map;

public class CodeNormalizer {

    public static void normalizeAST(CompilationUnit cu) {
        // This visitor finds all parameters and renames them to VARx
        cu.accept(new ModifierVisitor<Void>() {
            int counter = 1;
            Map<String, String> renameMap = new HashMap<>();

            @Override
            public Visitable visit(Parameter n, Void arg) {
                String oldName = n.getNameAsString();
                // If this is a new parameter, assign it the next "VAR" ID
                String newName = renameMap.computeIfAbsent(oldName, k -> "VAR" + counter++);
                n.setName(newName);
                return super.visit(n, arg);
            }
        }, null);
    }
}
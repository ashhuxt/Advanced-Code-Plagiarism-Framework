package com.plagiarism.detector.util;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import java.util.ArrayList;
import java.util.List;

public class ASTGenerator {

    // Generate AST from code
    public static CompilationUnit generateAST(String code) {
        try {
            return StaticJavaParser.parse(code);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Java Code: " + e.getMessage());
        }
    }

    // New method: Preorder Traversal for comparison
    public static List<String> getPreorderTraversal(CompilationUnit cu) {
        List<String> traversal = new ArrayList<>();
        preorder(cu, traversal);
        return traversal;
    }

    private static void preorder(Node node, List<String> traversal) {
        traversal.add(node.getClass().getSimpleName());
        for (Node child : node.getChildNodes()) {
            preorder(child, traversal);
        }
    }

    // Existing method
    public static List<String> extractNodeTypes(CompilationUnit cu) {
        List<String> nodeTypes = new ArrayList<>();
        collectNodes(cu, nodeTypes);
        return nodeTypes;
    }

    private static void collectNodes(Node node, List<String> nodeTypes) {
        nodeTypes.add(node.getClass().getSimpleName());
        for (Node child : node.getChildNodes()) {
            collectNodes(child, nodeTypes);
        }
    }
}
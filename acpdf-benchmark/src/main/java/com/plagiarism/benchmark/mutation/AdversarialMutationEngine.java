package com.plagiarism.benchmark.mutation;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Applies code transformations to test system robustness against obfuscation.
 * Implements various mutation strategies to simulate plagiarism evasion attempts.
 */
@Component
public class AdversarialMutationEngine {
    
    private final Random random = new Random();
    
    /**
     * Apply a specific mutation type to the given source code.
     */
    public String applyMutation(String sourceCode, MutationType mutationType) {
        return switch (mutationType) {
            case VARIABLE_RENAMING -> applyVariableRenaming(sourceCode);
            case DEAD_CODE_INJECTION -> applyDeadCodeInjection(sourceCode);
            case STATEMENT_REORDERING -> applyStatementReordering(sourceCode);
            case LOOP_TRANSFORMATION -> applyLoopTransformation(sourceCode);
            case ARITHMETIC_EQUIVALENCE -> applyArithmeticEquivalence(sourceCode);
            case PARENTHESIS_MODIFICATION -> applyParenthesisModification(sourceCode);
            case ACCESS_MODIFIER_CHANGE -> applyAccessModifierChange(sourceCode);
            case METHOD_REFACTORING -> applyMethodRefactoring(sourceCode);
            case COMBINED -> applyCombinedMutations(sourceCode);
        };
    }
    
    /**
     * Rename variables to different identifiers while preserving semantics.
     */
    public String applyVariableRenaming(String sourceCode) {
        Map<String, String> renamingMap = new HashMap<>();
        String result = sourceCode;
        
        // Pattern to match variable declarations: type variableName = ...
        Pattern declPattern = Pattern.compile("\\b(int|String|double|boolean|char|long|float)\\s+(\\w+)\\s*[=;]");
        Matcher matcher = declPattern.matcher(result);
        
        StringBuffer sb = new StringBuffer();
        int counter = 0;
        while (matcher.find()) {
            String originalVar = matcher.group(2);
            if (!renamingMap.containsKey(originalVar) && !isKeyword(originalVar)) {
                String newName = "var_" + counter++;
                renamingMap.put(originalVar, newName);
            }
        }
        
        // Apply renamings
        for (Map.Entry<String, String> entry : renamingMap.entrySet()) {
            result = result.replaceAll("\\b" + Pattern.quote(entry.getKey()) + "\\b", entry.getValue());
        }
        
        return result;
    }
    
    /**
     * Inject dead code that doesn't affect program behavior.
     */
    public String applyDeadCodeInjection(String sourceCode) {
        String[] deadCodeSnippets = {
            "// Dead code: int unused = 0;",
            "/* Unreachable: System.out.println(\"debug\"); */",
            "// Removed optimization hint",
            "/* Dead branch: if (false) { doSomething(); } */",
            "// Legacy code placeholder"
        };
        
        // Insert dead code at random positions (after opening braces)
        String[] lines = sourceCode.split("\n");
        List<String> modifiedLines = new ArrayList<>();
        
        for (String line : lines) {
            modifiedLines.add(line);
            if (line.trim().endsWith("{") && random.nextDouble() < 0.3) {
                String deadCode = deadCodeSnippets[random.nextInt(deadCodeSnippets.length)];
                String indent = getIndent(line) + "    ";
                modifiedLines.add(indent + deadCode);
            }
        }
        
        return String.join("\n", modifiedLines);
    }
    
    /**
     * Reorder independent statements where possible.
     */
    public String applyStatementReordering(String sourceCode) {
        // Simple implementation: swap consecutive independent assignments
        String[] lines = sourceCode.split("\n");
        List<String> modifiedLines = new ArrayList<>(Arrays.asList(lines));
        
        for (int i = 0; i < modifiedLines.size() - 1; i++) {
            if (random.nextDouble() < 0.2) {
                String line1 = modifiedLines.get(i).trim();
                String line2 = modifiedLines.get(i + 1).trim();
                
                // Check if both are simple assignments without dependencies
                if (isSimpleAssignment(line1) && isSimpleAssignment(line2)) {
                    Collections.swap(modifiedLines, i, i + 1);
                }
            }
        }
        
        return String.join("\n", modifiedLines);
    }
    
    /**
     * Transform loop constructs (for <-> while).
     */
    public String applyLoopTransformation(String sourceCode) {
        // Convert simple for loops to while loops
        Pattern forPattern = Pattern.compile(
            "for\\s*\\(\\s*(int|var)\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\2\\s*<\\s*(\\d+)\\s*;\\s*\\2\\+\\+\\s*\\)"
        );
        
        Matcher matcher = forPattern.matcher(sourceCode);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String type = matcher.group(1);
            String var = matcher.group(2);
            String start = matcher.group(3);
            String end = matcher.group(4);
            
            String replacement = type + " " + var + " = " + start + "; while (" + var + " < " + end + ")";
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    /**
     * Replace arithmetic expressions with equivalent forms.
     */
    public String applyArithmeticEquivalence(String sourceCode) {
        String result = sourceCode;
        
        // Replace x + 0 with x
        result = result.replaceAll("(\\w+)\\s*\\+\\s*0", "$1");
        
        // Replace x * 1 with x
        result = result.replaceAll("(\\w+)\\s*\\*\\s*1", "$1");
        
        // Replace x * 2 with x + x (sometimes)
        if (random.nextBoolean()) {
            result = result.replaceAll("(\\w+)\\s*\\*\\s*2", "$1 + $1");
        }
        
        // Replace x / 1 with x
        result = result.replaceAll("(\\w+)\\s*/\\s*1", "$1");
        
        return result;
    }
    
    /**
     * Add or remove unnecessary parentheses.
     */
    public String applyParenthesisModification(String sourceCode) {
        String result = sourceCode;
        
        // Add parentheses around simple expressions
        if (random.nextBoolean()) {
            result = result.replaceAll("(\\w+\\s*[+\\-*/]\\s*\\w+)", "($1)");
        }
        
        return result;
    }
    
    /**
     * Change access modifiers where semantically equivalent.
     */
    public String applyAccessModifierChange(String sourceCode) {
        String result = sourceCode;
        
        // Change public to protected in some cases (not for top-level classes)
        if (random.nextBoolean()) {
            result = result.replaceAll("public\\s+(static)?\\s+void", "protected $1 void");
        }
        
        return result;
    }
    
    /**
     * Apply method refactoring (simple inlining simulation).
     */
    public String applyMethodRefactoring(String sourceCode) {
        // This is a simplified version - real implementation would need AST parsing
        return sourceCode;
    }
    
    /**
     * Apply multiple mutations in sequence for robustness testing.
     */
    public String applyCombinedMutations(String sourceCode) {
        String result = sourceCode;
        
        // Apply 2-4 random mutations
        int numMutations = random.nextInt(3) + 2;
        MutationType[] types = MutationType.values();
        
        Set<MutationType> applied = new HashSet<>();
        for (int i = 0; i < numMutations; i++) {
            MutationType type;
            do {
                type = types[random.nextInt(types.length)];
            } while (type == MutationType.COMBINED || applied.contains(type));
            
            applied.add(type);
            result = applyMutation(result, type);
        }
        
        return result;
    }
    
    /**
     * Generate mutated variants of a code pair for robustness testing.
     */
    public List<String> generateMutatedVariants(String sourceCode, MutationType mutationType, int count) {
        List<String> variants = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            variants.add(applyMutation(sourceCode, mutationType));
        }
        return variants;
    }
    
    // Helper methods
    
    private boolean isKeyword(String word) {
        Set<String> keywords = Set.of(
            "public", "private", "protected", "static", "final", "class", "interface",
            "void", "int", "String", "double", "boolean", "char", "long", "float",
            "if", "else", "for", "while", "do", "switch", "case", "break", "continue",
            "return", "new", "this", "super", "extends", "implements", "import", "package"
        );
        return keywords.contains(word);
    }
    
    private boolean isSimpleAssignment(String line) {
        return line.matches("^\\s*\\w+\\s*=\\s*[^;]+;\\s*$") && 
               !line.contains("(") && !line.contains("[");
    }
    
    private String getIndent(String line) {
        int spaces = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') spaces++;
            else if (c == '\t') spaces += 4;
            else break;
        }
        return " ".repeat(spaces);
    }
}

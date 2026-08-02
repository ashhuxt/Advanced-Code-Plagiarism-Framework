package com.plagiarism.benchmark.mutation;

/**
 * Enumeration of supported mutation types for adversarial testing.
 */
public enum MutationType {
    
    /**
     * Rename variables to different identifiers
     */
    VARIABLE_RENAMING("Variable Renaming"),
    
    /**
     * Insert dead code that doesn't affect program behavior
     */
    DEAD_CODE_INJECTION("Dead Code Injection"),
    
    /**
     * Reorder independent statements
     */
    STATEMENT_REORDERING("Statement Reordering"),
    
    /**
     * Change loop constructs (for <-> while)
     */
    LOOP_TRANSFORMATION("Loop Transformation"),
    
    /**
     * Replace arithmetic expressions with equivalent forms
     */
    ARITHMETIC_EQUIVALENCE("Arithmetic Equivalence"),
    
    /**
     * Add or remove unnecessary parentheses
     */
    PARENTHESIS_MODIFICATION("Parenthesis Modification"),
    
    /**
     * Change access modifiers where semantically equivalent
     */
    ACCESS_MODIFIER_CHANGE("Access Modifier Change"),
    
    /**
     * Extract or inline methods
     */
    METHOD_REFACTORING("Method Refactoring"),
    
    /**
     * Combined mutations for robustness testing
     */
    COMBINED("Combined Mutations");
    
    private final String displayName;
    
    MutationType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

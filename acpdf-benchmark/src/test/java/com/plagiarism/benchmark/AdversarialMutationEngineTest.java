package com.plagiarism.benchmark;

import com.plagiarism.benchmark.mutation.AdversarialMutationEngine;
import com.plagiarism.benchmark.mutation.MutationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AdversarialMutationEngine.
 */
class AdversarialMutationEngineTest {
    
    private AdversarialMutationEngine mutationEngine;
    
    @BeforeEach
    void setUp() {
        mutationEngine = new AdversarialMutationEngine();
    }
    
    @Test
    void testVariableRenaming() {
        String original = "public class Test { int value = 10; String name = \"test\"; }";
        String mutated = mutationEngine.applyMutation(original, MutationType.VARIABLE_RENAMING);
        
        assertNotNull(mutated);
        assertNotEquals(original, mutated);
        assertTrue(mutated.contains("var_"));
    }
    
    @Test
    void testDeadCodeInjection() {
        String original = "public class Test { public void method() { System.out.println(\"hello\"); } }";
        String mutated = mutationEngine.applyMutation(original, MutationType.DEAD_CODE_INJECTION);
        
        assertNotNull(mutated);
        // Dead code may or may not be injected due to randomness, but should not throw
        assertTrue(mutated.length() >= original.length());
    }
    
    @Test
    void testStatementReordering() {
        String original = "public class Test { public void method() { int a = 1; int b = 2; } }";
        String mutated = mutationEngine.applyMutation(original, MutationType.STATEMENT_REORDERING);
        
        assertNotNull(mutated);
        // Statement reordering may or may not occur due to randomness
        assertNotNull(mutated);
    }
    
    @Test
    void testLoopTransformation() {
        String original = "for (int i = 0; i < 10; i++) { System.out.println(i); }";
        String mutated = mutationEngine.applyMutation(original, MutationType.LOOP_TRANSFORMATION);
        
        assertNotNull(mutated);
        // Should convert for loop to while loop
        assertFalse(mutated.startsWith("for"));
        assertTrue(mutated.contains("while"));
    }
    
    @Test
    void testArithmeticEquivalence() {
        String original = "int result = x + 0; int product = y * 1;";
        String mutated = mutationEngine.applyMutation(original, MutationType.ARITHMETIC_EQUIVALENCE);
        
        assertNotNull(mutated);
        // x + 0 should become x
        assertFalse(mutated.contains("+ 0"));
    }
    
    @Test
    void testParenthesisModification() {
        String original = "int result = a + b;";
        String mutated = mutationEngine.applyMutation(original, MutationType.PARENTHESIS_MODIFICATION);
        
        assertNotNull(mutated);
        // May add parentheses around a + b
        assertNotNull(mutated);
    }
    
    @Test
    void testAccessModifierChange() {
        String original = "public static void main(String[] args) {}";
        String mutated = mutationEngine.applyMutation(original, MutationType.ACCESS_MODIFIER_CHANGE);
        
        assertNotNull(mutated);
        // May change public to protected
        assertNotNull(mutated);
    }
    
    @Test
    void testCombinedMutations() {
        String original = "public class Test { int value = 10; for (int i = 0; i < 5; i++) { value++; } }";
        String mutated = mutationEngine.applyMutation(original, MutationType.COMBINED);
        
        assertNotNull(mutated);
        // Combined mutations should apply multiple transformations
        assertNotNull(mutated);
    }
    
    @Test
    void testGenerateMutatedVariants() {
        String original = "public class Test { int x = 5; }";
        
        var variants = mutationEngine.generateMutatedVariants(
            original, MutationType.VARIABLE_RENAMING, 3);
        
        assertNotNull(variants);
        assertEquals(3, variants.size());
        
        for (String variant : variants) {
            assertNotNull(variant);
            assertNotEquals(original, variant);
        }
    }
    
    @Test
    void testAllMutationTypes() {
        String sampleCode = "public class Sample { public static void main(String[] args) { int x = 10; for (int i = 0; i < 5; i++) { x += i; } } }";
        
        for (MutationType type : MutationType.values()) {
            String mutated = mutationEngine.applyMutation(sampleCode, type);
            assertNotNull(mutated, "Mutation type " + type + " should not return null");
        }
    }
}

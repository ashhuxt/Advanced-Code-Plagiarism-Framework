package com.plagiarism.benchmark.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single code pair in the benchmark dataset.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodePair {
    
    /**
     * Unique identifier for this code pair
     */
    private String id;
    
    /**
     * Source code content (the submission to check)
     */
    private String sourceCode;
    
    /**
     * Target code content (the reference or suspected original)
     */
    private String targetCode;
    
    /**
     * Expected similarity verdict (e.g., "PLAGIARISM", "NO_PLAGIARISM")
     */
    private String expectedVerdict;
    
    /**
     * Expected similarity percentage (ground truth)
     */
    private double expectedSimilarity;
    
    /**
     * Metadata about the code pair (e.g., difficulty, mutation type)
     */
    private String metadata;
    
    /**
     * Source file name
     */
    private String sourceFileName;
    
    /**
     * Target file name
     */
    private String targetFileName;
}

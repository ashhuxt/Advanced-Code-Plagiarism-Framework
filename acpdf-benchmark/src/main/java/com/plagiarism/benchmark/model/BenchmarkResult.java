package com.plagiarism.benchmark.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the result of running plagiarism detection on a code pair.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkResult {
    
    /**
     * The code pair that was evaluated
     */
    private CodePair codePair;
    
    /**
     * Actual similarity percentage computed by the detector
     */
    private double actualSimilarity;
    
    /**
     * Actual verdict from the detector
     */
    private String actualVerdict;
    
    /**
     * Whether the detection matched the expected result
     */
    private boolean correctDetection;
    
    /**
     * Processing time in milliseconds
     */
    private long processingTimeMs;
    
    /**
     * Error message if processing failed
     */
    private String errorMessage;
    
    /**
     * True if this is a positive case (plagiarism exists)
     */
    public boolean isPositiveCase() {
        return "PLAGIARISM".equalsIgnoreCase(codePair.getExpectedVerdict());
    }
    
    /**
     * True if detection correctly identified plagiarism
     */
    public boolean isTruePositive() {
        return isPositiveCase() && "PLAGIARISM".equalsIgnoreCase(actualVerdict);
    }
    
    /**
     * True if detection correctly identified no plagiarism
     */
    public boolean isTrueNegative() {
        return !isPositiveCase() && !"PLAGIARISM".equalsIgnoreCase(actualVerdict);
    }
    
    /**
     * True if false alarm (detected plagiarism when none exists)
     */
    public boolean isFalsePositive() {
        return !isPositiveCase() && "PLAGIARISM".equalsIgnoreCase(actualVerdict);
    }
    
    /**
     * True if missed plagiarism
     */
    public boolean isFalseNegative() {
        return isPositiveCase() && !"PLAGIARISM".equalsIgnoreCase(actualVerdict);
    }
}

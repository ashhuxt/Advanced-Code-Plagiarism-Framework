package com.plagiarism.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the result of similarity computation between code samples.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimilarityResult {
    
    /**
     * Overall similarity score (0.0 to 1.0)
     */
    private double overallScore;
    
    /**
     * LCS-based similarity score
     */
    private Double lcsScore;
    
    /**
     * AST-based similarity score
     */
    private Double astScore;
    
    /**
     * TF-IDF based similarity score
     */
    private Double tfidfScore;
    
    /**
     * Name of the strategy used for computation
     */
    private String strategyName;
    
    /**
     * Additional details about the comparison
     */
    private String details;
}

package com.plagiarism.core.api;

/**
 * Strategy interface for similarity computation algorithms.
 * Allows dynamic switching between different similarity algorithms
 * (e.g., LCS-based, AST-based, TF-IDF-based).
 */
public interface SimilarityStrategy {
    
    /**
     * Compute similarity score between two code representations.
     * 
     * @param code1 First code representation
     * @param code2 Second code representation
     * @return Similarity score between 0.0 and 1.0
     */
    double computeSimilarity(String code1, String code2);
    
    /**
     * Get the name of this strategy for identification.
     * 
     * @return Strategy name
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}

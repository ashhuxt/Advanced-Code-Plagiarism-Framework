package com.plagiarism.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents extracted features from code analysis.
 * Contains AST representation, TF-IDF vectors, and other feature data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureSet {
    
    /**
     * Abstract Syntax Tree representation
     */
    private String astRepresentation;
    
    /**
     * TF-IDF vector as a map of term to weight
     */
    private Map<String, Double> tfidfVector;
    
    /**
     * Normalized token sequence
     */
    private String[] tokens;
    
    /**
     * Additional metadata about the features
     */
    private Map<String, Object> metadata;
}

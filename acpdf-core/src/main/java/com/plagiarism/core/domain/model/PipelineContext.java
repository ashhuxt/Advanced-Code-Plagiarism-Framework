package com.plagiarism.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the context data passed through the pipeline stages.
 * Contains all intermediate results and final output.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineContext {
    
    /**
     * Raw input code content
     */
    private String rawCode;
    
    /**
     * Normalized code after preprocessing
     */
    private String normalizedCode;
    
    /**
     * Extracted features (AST representation, TF-IDF vectors, etc.)
     */
    private FeatureSet features;
    
    /**
     * Computed similarity scores
     */
    private SimilarityResult similarityResult;
    
    /**
     * Final plagiarism detection report
     */
    private PlagiarismReport report;
    
    /**
     * Flag indicating if processing completed successfully
     */
    private boolean success;
    
    /**
     * Error message if processing failed
     */
    private String errorMessage;
    
    /**
     * Create a new context with raw code
     */
    public static PipelineContext fromRawCode(String rawCode) {
        return PipelineContext.builder()
                .rawCode(rawCode)
                .success(false)
                .build();
    }
}

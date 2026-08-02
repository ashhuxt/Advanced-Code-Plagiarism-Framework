package com.plagiarism.core.api;

import com.plagiarism.core.domain.model.PipelineContext;

/**
 * Generic Pipeline interface that orchestrates the processing of code
 * through sequential stages.
 */
public interface Pipeline {
    
    /**
     * Execute the pipeline with the given context.
     * 
     * @param context The pipeline context containing input and intermediate results
     * @return The processed context with final results
     */
    PipelineContext execute(PipelineContext context);
    
    /**
     * Register a new stage to the pipeline.
     * 
     * @param stage The stage to add
     * @return This pipeline for method chaining
     */
    Pipeline addStage(PipelineStage<?, ?> stage);
    
    /**
     * Get the name of this pipeline configuration.
     * 
     * @return Pipeline name
     */
    String getName();
}

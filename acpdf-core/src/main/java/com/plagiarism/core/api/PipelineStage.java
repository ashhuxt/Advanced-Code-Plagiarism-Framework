package com.plagiarism.core.api;

/**
 * Represents a processing stage in the plagiarism detection pipeline.
 * Each stage receives input of type T and produces output of type R.
 * 
 * @param <T> Input type for this stage
 * @param <R> Output type from this stage
 */
public interface PipelineStage<T, R> {
    
    /**
     * Process the input and produce output.
     * 
     * @param input The input to process
     * @return The processed output
     */
    R process(T input);
    
    /**
     * Get the name of this stage for logging and identification.
     * 
     * @return The stage name
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}

package com.plagiarism.engine.pipeline;

import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.PipelineContext;
import com.plagiarism.core.pipeline.AbstractPipeline;
import com.plagiarism.engine.pipeline.stages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the PlagiarismDetectionPipeline.
 * Configures the standard sequence of stages for code plagiarism detection.
 */
public class PlagiarismDetectionPipeline extends AbstractPipeline {
    
    private static final Logger logger = LoggerFactory.getLogger(PlagiarismDetectionPipeline.class);
    
    public PlagiarismDetectionPipeline() {
        super("PlagiarismDetectionPipeline");
        configureDefaultStages();
    }
    
    /**
     * Configure the default stages in the correct order.
     */
    private void configureDefaultStages() {
        // Stage 1: Input - Read and validate code content
        addStage(new InputStage());
        
        // Stage 2: Normalization - Standardize syntax
        addStage(new NormalizationStage());
        
        // Stage 3: Feature Extraction - Extract AST and TF-IDF features
        addStage(new FeatureExtractionStage());
        
        // Stage 4: Similarity Engine - Compute alignment scores
        addStage(new SimilarityEngineStage());
        
        // Stage 5: Report Generator - Output aggregated results
        addStage(new ReportGeneratorStage());
        
        logger.info("PlagiarismDetectionPipeline: Configured with {} default stages", getStages().size());
    }
    
    /**
     * Create a custom pipeline without default stages.
     * Use addStage() to configure custom stage sequence.
     */
    public PlagiarismDetectionPipeline(boolean skipDefaults) {
        super("CustomPlagiarismPipeline");
        if (!skipDefaults) {
            configureDefaultStages();
        }
    }
    
    /**
     * Execute the pipeline with raw code input.
     * Convenience method that creates context internally.
     * 
     * @param rawCode The raw code to analyze
     * @return The processed context with results
     */
    public PipelineContext execute(String rawCode) {
        PipelineContext context = PipelineContext.fromRawCode(rawCode);
        return execute(context);
    }
    
    /**
     * Replace the similarity strategy at runtime.
     * 
     * @param strategy The new similarity strategy
     * @return This pipeline for method chaining
     */
    public PlagiarismDetectionPipeline withSimilarityStrategy(PipelineStage<?, ?> strategyStage) {
        // Remove existing SimilarityEngineStage if present
        getStages().removeIf(stage -> stage instanceof SimilarityEngineStage);
        
        // Add the new strategy stage
        if (strategyStage instanceof SimilarityEngineStage) {
            addStage(strategyStage);
        } else {
            logger.warn("Provided stage is not a SimilarityEngineStage, adding anyway");
            addStage(strategyStage);
        }
        
        return this;
    }
}

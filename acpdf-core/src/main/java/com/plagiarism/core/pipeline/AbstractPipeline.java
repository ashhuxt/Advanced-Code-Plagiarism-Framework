package com.plagiarism.core.pipeline;

import com.plagiarism.core.api.Pipeline;
import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation of the Pipeline pattern.
 * Provides common functionality for pipeline execution and stage management.
 */
public abstract class AbstractPipeline implements Pipeline {
    
    protected static final Logger logger = LoggerFactory.getLogger(AbstractPipeline.class);
    
    protected final List<PipelineStage<?, ?>> stages;
    protected final String name;
    
    protected AbstractPipeline(String name) {
        this.name = name;
        this.stages = new ArrayList<>();
    }
    
    @Override
    public Pipeline addStage(PipelineStage<?, ?> stage) {
        this.stages.add(stage);
        logger.debug("Added stage: {}", stage.getName());
        return this;
    }
    
    @Override
    public PipelineContext execute(PipelineContext context) {
        logger.info("Starting pipeline execution: {}", name);
        
        try {
            for (PipelineStage<?, ?> stage : stages) {
                if (!context.isSuccess() && context.getErrorMessage() != null) {
                    logger.warn("Pipeline stopped due to previous error at stage: {}", stage.getName());
                    break;
                }
                
                logger.debug("Executing stage: {}", stage.getName());
                context = processStage(stage, context);
                
                if (!context.isSuccess()) {
                    logger.error("Stage {} failed: {}", stage.getName(), context.getErrorMessage());
                    break;
                }
            }
            
            if (context.isSuccess()) {
                logger.info("Pipeline {} completed successfully", name);
            } else {
                logger.error("Pipeline {} completed with errors", name);
            }
            
        } catch (Exception e) {
            logger.error("Pipeline {} encountered unexpected error: {}", name, e.getMessage(), e);
            context.setSuccess(false);
            context.setErrorMessage(e.getMessage());
        }
        
        return context;
    }
    
    /**
     * Process a single stage with type-safe casting.
     * Subclasses may override for custom processing logic.
     */
    @SuppressWarnings("unchecked")
    protected PipelineContext processStage(PipelineStage<?, ?> stage, PipelineContext context) {
        try {
            // Cast to PipelineStage<PipelineContext, PipelineContext> for uniform processing
            PipelineStage<PipelineContext, PipelineContext> typedStage = 
                (PipelineStage<PipelineContext, PipelineContext>) stage;
            return typedStage.process(context);
        } catch (ClassCastException e) {
            logger.error("Stage {} has incompatible types", stage.getName(), e);
            context.setSuccess(false);
            context.setErrorMessage("Stage type mismatch: " + e.getMessage());
            return context;
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    protected List<PipelineStage<?, ?>> getStages() {
        return new ArrayList<>(stages);
    }
}

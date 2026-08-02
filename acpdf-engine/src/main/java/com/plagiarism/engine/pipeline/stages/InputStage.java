package com.plagiarism.engine.pipeline.stages;

import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input Stage: Reads and validates code content.
 * This is the first stage in the plagiarism detection pipeline.
 */
public class InputStage implements PipelineStage<PipelineContext, PipelineContext> {
    
    private static final Logger logger = LoggerFactory.getLogger(InputStage.class);
    
    @Override
    public PipelineContext process(PipelineContext context) {
        logger.debug("InputStage: Processing raw code input");
        
        try {
            String rawCode = context.getRawCode();
            
            if (rawCode == null || rawCode.trim().isEmpty()) {
                context.setSuccess(false);
                context.setErrorMessage("Input code is null or empty");
                logger.error("InputStage: {}", context.getErrorMessage());
                return context;
            }
            
            // Validate minimum code length
            if (rawCode.trim().length() < 10) {
                context.setSuccess(false);
                context.setErrorMessage("Input code is too short (minimum 10 characters required)");
                logger.error("InputStage: {}", context.getErrorMessage());
                return context;
            }
            
            // Log basic statistics
            int lineCount = rawCode.split("\n").length;
            int charCount = rawCode.length();
            logger.info("InputStage: Loaded {} lines, {} characters", lineCount, charCount);
            
            context.setSuccess(true);
            
        } catch (Exception e) {
            context.setSuccess(false);
            context.setErrorMessage("Failed to read input code: " + e.getMessage());
            logger.error("InputStage: Error processing input", e);
        }
        
        return context;
    }
    
    @Override
    public String getName() {
        return "InputStage";
    }
}

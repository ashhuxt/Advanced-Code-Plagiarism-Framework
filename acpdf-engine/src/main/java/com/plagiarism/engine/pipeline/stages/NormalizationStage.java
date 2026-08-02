package com.plagiarism.engine.pipeline.stages;

import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Normalization Stage: Standardizes code syntax by removing inconsistencies.
 * Performs whitespace normalization, comment removal, and identifier standardization.
 */
public class NormalizationStage implements PipelineStage<PipelineContext, PipelineContext> {
    
    private static final Logger logger = LoggerFactory.getLogger(NormalizationStage.class);
    
    // Pattern to match single-line comments
    private static final Pattern SINGLE_LINE_COMMENT = Pattern.compile("//.*$", Pattern.MULTILINE);
    
    // Pattern to match multi-line comments
    private static final Pattern MULTI_LINE_COMMENT = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", Pattern.DOTALL);
    
    // Pattern to match multiple whitespace
    private static final Pattern MULTIPLE_WHITESPACE = Pattern.compile("\\s+");
    
    @Override
    public PipelineContext process(PipelineContext context) {
        logger.debug("NormalizationStage: Starting code normalization");
        
        try {
            if (!context.isSuccess()) {
                logger.warn("NormalizationStage: Skipping due to previous error");
                return context;
            }
            
            String rawCode = context.getRawCode();
            String normalized = rawCode;
            
            // Remove single-line comments
            normalized = SINGLE_LINE_COMMENT.matcher(normalized).replaceAll("");
            logger.trace("NormalizationStage: Removed single-line comments");
            
            // Remove multi-line comments
            normalized = MULTI_LINE_COMMENT.matcher(normalized).replaceAll("");
            logger.trace("NormalizationStage: Removed multi-line comments");
            
            // Normalize whitespace (multiple spaces/tabs to single space)
            normalized = MULTIPLE_WHITESPACE.matcher(normalized).replaceAll(" ");
            logger.trace("NormalizationStage: Normalized whitespace");
            
            // Trim leading/trailing whitespace from each line
            StringBuilder result = new StringBuilder();
            for (String line : normalized.split("\n")) {
                result.append(line.trim()).append("\n");
            }
            normalized = result.toString().trim();
            
            // Normalize string literals (replace content with placeholder)
            normalized = normalizeStringLiterals(normalized);
            logger.trace("NormalizationStage: Normalized string literals");
            
            context.setNormalizedCode(normalized);
            context.setSuccess(true);
            
            int originalLength = rawCode.length();
            int normalizedLength = normalized.length();
            logger.info("NormalizationStage: Reduced code from {} to {} characters ({:.1f}% reduction)", 
                    originalLength, normalizedLength, 
                    (1.0 - (double) normalizedLength / originalLength) * 100);
            
        } catch (Exception e) {
            context.setSuccess(false);
            context.setErrorMessage("Failed to normalize code: " + e.getMessage());
            logger.error("NormalizationStage: Error during normalization", e);
        }
        
        return context;
    }
    
    /**
     * Replace string literal contents with a placeholder to avoid false positives
     * from similar string values.
     */
    private String normalizeStringLiterals(String code) {
        // Replace string literals with placeholder
        return code.replaceAll("\"[^\"]*\"", "\"STR\"");
    }
    
    @Override
    public String getName() {
        return "NormalizationStage";
    }
}

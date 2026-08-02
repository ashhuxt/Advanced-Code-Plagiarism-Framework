package com.plagiarism.engine.pipeline.stages;

import com.plagiarism.core.api.SimilarityStrategy;
import com.plagiarism.core.domain.model.PipelineContext;
import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.SimilarityResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Similarity Engine Stage: Computes alignment scores using the configured strategy.
 * Implements the Strategy Pattern to allow dynamic switching of similarity algorithms.
 */
public class SimilarityEngineStage implements PipelineStage<PipelineContext, PipelineContext> {
    
    private static final Logger logger = LoggerFactory.getLogger(SimilarityEngineStage.class);
    
    private SimilarityStrategy strategy;
    
    /**
     * Create stage with default strategy.
     */
    public SimilarityEngineStage() {
        this.strategy = new DefaultSimilarityStrategy();
    }
    
    /**
     * Create stage with custom strategy.
     * 
     * @param strategy The similarity strategy to use
     */
    public SimilarityEngineStage(SimilarityStrategy strategy) {
        this.strategy = strategy;
    }
    
    @Override
    public PipelineContext process(PipelineContext context) {
        logger.debug("SimilarityEngineStage: Computing similarity with strategy: {}", strategy.getName());
        
        try {
            if (!context.isSuccess()) {
                logger.warn("SimilarityEngineStage: Skipping due to previous error");
                return context;
            }
            
            // For single-file analysis, we compare against a reference or self-compare
            // In real usage, this would compare two different code samples
            String normalizedCode = context.getNormalizedCode();
            if (normalizedCode == null || normalizedCode.trim().isEmpty()) {
                context.setSuccess(false);
                context.setErrorMessage("No normalized code available for similarity computation");
                logger.error("SimilarityEngineStage: {}", context.getErrorMessage());
                return context;
            }
            
            // Compute similarity (placeholder - compares code to itself for demo)
            // In actual usage, this would compare two different code samples
            double similarityScore = strategy.computeSimilarity(normalizedCode, normalizedCode);
            
            // Build similarity result
            SimilarityResult result = SimilarityResult.builder()
                    .overallScore(similarityScore)
                    .strategyName(strategy.getName())
                    .details("Computed using " + strategy.getName())
                    .build();
            
            context.setSimilarityResult(result);
            context.setSuccess(true);
            
            logger.info("SimilarityEngineStage: Computed similarity score: {:.2f} using {}", 
                    similarityScore, strategy.getName());
            
        } catch (Exception e) {
            context.setSuccess(false);
            context.setErrorMessage("Failed to compute similarity: " + e.getMessage());
            logger.error("SimilarityEngineStage: Error during similarity computation", e);
        }
        
        return context;
    }
    
    /**
     * Set a new similarity strategy at runtime.
     * 
     * @param strategy The new strategy to use
     */
    public void setStrategy(SimilarityStrategy strategy) {
        this.strategy = strategy;
        logger.info("SimilarityEngineStage: Strategy changed to {}", strategy.getName());
    }
    
    @Override
    public String getName() {
        return "SimilarityEngineStage";
    }
    
    /**
     * Default similarity strategy implementation.
     * Uses simple string comparison as a placeholder.
     */
    private static class DefaultSimilarityStrategy implements SimilarityStrategy {
        
        @Override
        public double computeSimilarity(String code1, String code2) {
            if (code1 == null || code2 == null) {
                return 0.0;
            }
            
            if (code1.equals(code2)) {
                return 1.0;
            }
            
            // Simple character-based similarity (Levenshtein-based placeholder)
            int maxLen = Math.max(code1.length(), code2.length());
            if (maxLen == 0) return 1.0;
            
            int matches = 0;
            int minLen = Math.min(code1.length(), code2.length());
            for (int i = 0; i < minLen; i++) {
                if (code1.charAt(i) == code2.charAt(i)) {
                    matches++;
                }
            }
            
            return (double) matches / maxLen;
        }
        
        @Override
        public String getName() {
            return "DefaultSimilarityStrategy";
        }
    }
}

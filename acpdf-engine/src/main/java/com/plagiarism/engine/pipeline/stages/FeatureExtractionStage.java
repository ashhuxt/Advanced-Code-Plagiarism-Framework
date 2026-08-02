package com.plagiarism.engine.pipeline.stages;

import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.FeatureSet;
import com.plagiarism.core.domain.model.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Feature Extraction Stage: Invokes AST and TF-IDF modules to extract features.
 * This stage generates the feature set used for similarity comparison.
 */
public class FeatureExtractionStage implements PipelineStage<PipelineContext, PipelineContext> {
    
    private static final Logger logger = LoggerFactory.getLogger(FeatureExtractionStage.class);
    
    // Placeholder for AST extractor - to be connected to acpdf-ast module
    private final AstExtractor astExtractor;
    
    // Placeholder for TF-IDF extractor - to be connected to acpdf-tfidf module
    private final TfidfExtractor tfidfExtractor;
    
    public FeatureExtractionStage() {
        this.astExtractor = new AstExtractor();
        this.tfidfExtractor = new TfidfExtractor();
    }
    
    @Override
    public PipelineContext process(PipelineContext context) {
        logger.debug("FeatureExtractionStage: Starting feature extraction");
        
        try {
            if (!context.isSuccess()) {
                logger.warn("FeatureExtractionStage: Skipping due to previous error");
                return context;
            }
            
            String normalizedCode = context.getNormalizedCode();
            if (normalizedCode == null || normalizedCode.trim().isEmpty()) {
                context.setSuccess(false);
                context.setErrorMessage("No normalized code available for feature extraction");
                logger.error("FeatureExtractionStage: {}", context.getErrorMessage());
                return context;
            }
            
            // Extract AST representation
            logger.info("FeatureExtractionStage: Extracting AST representation");
            String astRepresentation = astExtractor.extract(normalizedCode);
            
            // Extract TF-IDF vector
            logger.info("FeatureExtractionStage: Computing TF-IDF vector");
            Map<String, Double> tfidfVector = tfidfExtractor.compute(normalizedCode);
            
            // Tokenize normalized code
            String[] tokens = tokenize(normalizedCode);
            
            // Build feature set
            FeatureSet features = FeatureSet.builder()
                    .astRepresentation(astRepresentation)
                    .tfidfVector(tfidfVector)
                    .tokens(tokens)
                    .metadata(new HashMap<>())
                    .build();
            
            context.setFeatures(features);
            context.setSuccess(true);
            
            logger.info("FeatureExtractionStage: Extracted {} TF-IDF terms, AST depth: {}", 
                    tfidfVector.size(), estimateAstDepth(astRepresentation));
            
        } catch (Exception e) {
            context.setSuccess(false);
            context.setErrorMessage("Failed to extract features: " + e.getMessage());
            logger.error("FeatureExtractionStage: Error during feature extraction", e);
        }
        
        return context;
    }
    
    /**
     * Simple tokenization by splitting on non-word characters.
     */
    private String[] tokenize(String code) {
        return code.split("\\W+");
    }
    
    /**
     * Estimate AST depth from representation (placeholder logic).
     */
    private int estimateAstDepth(String astRepresentation) {
        if (astRepresentation == null) return 0;
        // Count nesting indicators as a simple depth estimate
        return (int) astRepresentation.chars().filter(c -> c == '{' || c == '(').count();
    }
    
    @Override
    public String getName() {
        return "FeatureExtractionStage";
    }
    
    /**
     * Inner class for AST extraction - placeholder for acpdf-ast integration.
     */
    private static class AstExtractor {
        public String extract(String code) {
            // TODO: Integrate with acpdf-ast module
            // For now, return a placeholder representation
            return "AST_PLACEHOLDER:" + code.length() + "_nodes";
        }
    }
    
    /**
     * Inner class for TF-IDF computation - placeholder for acpdf-tfidf integration.
     */
    private static class TfidfExtractor {
        public Map<String, Double> compute(String code) {
            // TODO: Integrate with acpdf-tfidf module
            // For now, return a simple term frequency map as placeholder
            Map<String, Double> vector = new HashMap<>();
            String[] tokens = code.toLowerCase().split("\\W+");
            for (String token : tokens) {
                if (!token.isEmpty() && token.length() > 2) {
                    vector.put(token, vector.getOrDefault(token, 0.0) + 1.0);
                }
            }
            return vector;
        }
    }
}

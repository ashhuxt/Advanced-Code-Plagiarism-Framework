package com.plagiarism.engine.pipeline.stages;

import com.plagiarism.core.api.PipelineStage;
import com.plagiarism.core.domain.model.PipelineContext;
import com.plagiarism.core.domain.model.PlagiarismReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Report Generator Stage: Outputs aggregated results as a plagiarism report.
 * This is typically the final stage in the pipeline.
 */
public class ReportGeneratorStage implements PipelineStage<PipelineContext, PipelineContext> {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorStage.class);
    
    // Thresholds for verdict determination
    private static final double HIGH_SIMILARITY_THRESHOLD = 0.8;
    private static final double MEDIUM_SIMILARITY_THRESHOLD = 0.5;
    private static final double LOW_SIMILARITY_THRESHOLD = 0.2;
    
    @Override
    public PipelineContext process(PipelineContext context) {
        logger.debug("ReportGeneratorStage: Generating plagiarism report");
        
        try {
            if (!context.isSuccess()) {
                logger.warn("ReportGeneratorStage: Generating error report due to previous failure");
                return generateErrorReport(context);
            }
            
            if (context.getSimilarityResult() == null) {
                context.setSuccess(false);
                context.setErrorMessage("No similarity result available for report generation");
                logger.error("ReportGeneratorStage: {}", context.getErrorMessage());
                return context;
            }
            
            double similarityScore = context.getSimilarityResult().getOverallScore();
            
            // Determine verdict based on similarity score
            String verdict = determineVerdict(similarityScore);
            String confidenceLevel = determineConfidenceLevel(similarityScore);
            
            // Generate matched segments (placeholder - would come from actual comparison)
            List<PlagiarismReport.MatchedSegment> matchedSegments = generateMatchedSegments(context);
            
            // Build the report
            PlagiarismReport report = PlagiarismReport.builder()
                    .sourceFile("input.java")
                    .targetFile("reference.java")
                    .similarityPercentage(similarityScore * 100)
                    .matchedSegments(matchedSegments)
                    .verdict(verdict)
                    .confidenceLevel(confidenceLevel)
                    .summary(generateSummary(context, verdict))
                    .build();
            
            context.setReport(report);
            context.setSuccess(true);
            
            logger.info("ReportGeneratorStage: Generated report - Verdict: {}, Similarity: {:.1f}%", 
                    verdict, similarityScore * 100);
            
        } catch (Exception e) {
            context.setSuccess(false);
            context.setErrorMessage("Failed to generate report: " + e.getMessage());
            logger.error("ReportGeneratorStage: Error during report generation", e);
        }
        
        return context;
    }
    
    /**
     * Determine the plagiarism verdict based on similarity score.
     */
    private String determineVerdict(double similarityScore) {
        if (similarityScore >= HIGH_SIMILARITY_THRESHOLD) {
            return "PLAGIARISM_DETECTED";
        } else if (similarityScore >= MEDIUM_SIMILARITY_THRESHOLD) {
            return "SUSPECTED_PLAGIARISM";
        } else if (similarityScore >= LOW_SIMILARITY_THRESHOLD) {
            return "LOW_SIMILARITY";
        } else {
            return "NO_PLAGIARISM";
        }
    }
    
    /**
     * Determine confidence level based on similarity score.
     */
    private String determineConfidenceLevel(double similarityScore) {
        if (similarityScore >= HIGH_SIMILARITY_THRESHOLD) {
            return "HIGH";
        } else if (similarityScore >= MEDIUM_SIMILARITY_THRESHOLD) {
            return "MEDIUM";
        } else if (similarityScore >= LOW_SIMILARITY_THRESHOLD) {
            return "LOW";
        } else {
            return "VERY_LOW";
        }
    }
    
    /**
     * Generate placeholder matched segments.
     */
    private List<PlagiarismReport.MatchedSegment> generateMatchedSegments(PipelineContext context) {
        // In real implementation, this would analyze actual code matches
        // For now, return empty list or placeholder segments
        if (context.getSimilarityResult() != null && 
            context.getSimilarityResult().getOverallScore() > MEDIUM_SIMILARITY_THRESHOLD) {
            
            PlagiarismReport.MatchedSegment segment = PlagiarismReport.MatchedSegment.builder()
                    .sourceStartLine(1)
                    .sourceEndLine(10)
                    .targetStartLine(1)
                    .targetEndLine(10)
                    .sourceCode("// Matched segment placeholder")
                    .targetCode("// Matched segment placeholder")
                    .segmentSimilarity(context.getSimilarityResult().getOverallScore())
                    .build();
            
            return Collections.singletonList(segment);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Generate a summary of the analysis.
     */
    private String generateSummary(PipelineContext context, String verdict) {
        StringBuilder summary = new StringBuilder();
        summary.append("Analysis completed using strategy: ")
               .append(context.getSimilarityResult() != null ? 
                       context.getSimilarityResult().getStrategyName() : "unknown")
               .append(". ");
        
        switch (verdict) {
            case "PLAGIARISM_DETECTED":
                summary.append("High similarity detected. Manual review recommended.");
                break;
            case "SUSPECTED_PLAGIARISM":
                summary.append("Moderate similarity detected. Further investigation advised.");
                break;
            case "LOW_SIMILARITY":
                summary.append("Low similarity detected. Likely coincidental matches.");
                break;
            default:
                summary.append("No significant similarity detected.");
        }
        
        return summary.toString();
    }
    
    /**
     * Generate an error report when pipeline fails.
     */
    private PipelineContext generateErrorReport(PipelineContext context) {
        PlagiarismReport errorReport = PlagiarismReport.builder()
                .sourceFile("unknown")
                .targetFile("unknown")
                .similarityPercentage(0.0)
                .matchedSegments(new ArrayList<>())
                .verdict("ANALYSIS_FAILED")
                .confidenceLevel("NONE")
                .summary("Analysis failed: " + context.getErrorMessage())
                .build();
        
        context.setReport(errorReport);
        return context;
    }
    
    @Override
    public String getName() {
        return "ReportGeneratorStage";
    }
}

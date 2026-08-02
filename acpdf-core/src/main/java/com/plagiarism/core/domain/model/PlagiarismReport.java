package com.plagiarism.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the final plagiarism detection report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismReport {
    
    /**
     * Source file identifier
     */
    private String sourceFile;
    
    /**
     * Target file identifier (suspected plagiarism source)
     */
    private String targetFile;
    
    /**
     * Overall similarity percentage
     */
    private double similarityPercentage;
    
    /**
     * List of matched code segments
     */
    private List<MatchedSegment> matchedSegments;
    
    /**
     * Detection verdict (e.g., "PLAGIARISM_DETECTED", "NO_PLAGIARISM")
     */
    private String verdict;
    
    /**
     * Confidence level of the detection
     */
    private String confidenceLevel;
    
    /**
     * Detailed analysis summary
     */
    private String summary;
    
    /**
     * Represents a matched code segment between two files
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchedSegment {
        private int sourceStartLine;
        private int sourceEndLine;
        private int targetStartLine;
        private int targetEndLine;
        private String sourceCode;
        private String targetCode;
        private double segmentSimilarity;
    }
}

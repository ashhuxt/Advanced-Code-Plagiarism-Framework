package com.plagiarism.benchmark.metrics;

import com.plagiarism.benchmark.model.BenchmarkResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Calculates performance metrics for plagiarism detection benchmarking.
 * Computes Precision, Recall, F1-Score, and other statistical measures.
 */
@Slf4j
@Component
public class PerformanceMetricsCalculator {
    
    /**
     * Calculate all performance metrics from a list of benchmark results.
     */
    public MetricsSummary calculateMetrics(List<BenchmarkResult> results) {
        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("Results list cannot be empty");
        }
        
        int truePositives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;
        long totalProcessingTime = 0;
        int successfulDetections = 0;
        
        for (BenchmarkResult result : results) {
            if (result.getErrorMessage() != null) {
                log.warn("Skipping result with error: {}", result.getErrorMessage());
                continue;
            }
            
            successfulDetections++;
            totalProcessingTime += result.getProcessingTimeMs();
            
            if (result.isTruePositive()) {
                truePositives++;
            } else if (result.isTrueNegative()) {
                trueNegatives++;
            } else if (result.isFalsePositive()) {
                falsePositives++;
            } else if (result.isFalseNegative()) {
                falseNegatives++;
            }
        }
        
        // Calculate precision: TP / (TP + FP)
        double precision = (truePositives + falsePositives) > 0 
            ? (double) truePositives / (truePositives + falsePositives) 
            : 0.0;
        
        // Calculate recall: TP / (TP + FN)
        double recall = (truePositives + falseNegatives) > 0 
            ? (double) truePositives / (truePositives + falseNegatives) 
            : 0.0;
        
        // Calculate F1-Score: 2 * (Precision * Recall) / (Precision + Recall)
        double f1Score = (precision + recall) > 0 
            ? 2 * (precision * recall) / (precision + recall) 
            : 0.0;
        
        // Calculate accuracy: (TP + TN) / Total
        double accuracy = successfulDetections > 0 
            ? (double) (truePositives + trueNegatives) / successfulDetections 
            : 0.0;
        
        // Calculate average processing time
        double avgProcessingTime = successfulDetections > 0 
            ? (double) totalProcessingTime / successfulDetections 
            : 0.0;
        
        MetricsSummary summary = MetricsSummary.builder()
                .truePositives(truePositives)
                .trueNegatives(trueNegatives)
                .falsePositives(falsePositives)
                .falseNegatives(falseNegatives)
                .precision(precision)
                .recall(recall)
                .f1Score(f1Score)
                .accuracy(accuracy)
                .totalTests(successfulDetections)
                .averageProcessingTimeMs(avgProcessingTime)
                .build();
        
        log.info("Metrics Summary: Precision={}, Recall={}, F1-Score={}, Accuracy={}", 
                 String.format("%.4f", precision),
                 String.format("%.4f", recall),
                 String.format("%.4f", f1Score),
                 String.format("%.4f", accuracy));
        
        return summary;
    }
    
    /**
     * Calculate metrics for a specific mutation type.
     */
    public MetricsSummary calculateMetricsByMutationType(List<BenchmarkResult> results, String mutationType) {
        List<BenchmarkResult> filtered = results.stream()
                .filter(r -> mutationType.equals(r.getCodePair().getMetadata()))
                .toList();
        
        if (filtered.isEmpty()) {
            log.warn("No results found for mutation type: {}", mutationType);
            return MetricsSummary.builder().build();
        }
        
        return calculateMetrics(filtered);
    }
    
    /**
     * Print detailed metrics report.
     */
    public void printReport(MetricsSummary summary) {
        System.out.println("========================================");
        System.out.println("       PERFORMANCE METRICS REPORT       ");
        System.out.println("========================================");
        System.out.println("Total Tests:              " + summary.getTotalTests());
        System.out.println("----------------------------------------");
        System.out.println("True Positives:           " + summary.getTruePositives());
        System.out.println("True Negatives:           " + summary.getTrueNegatives());
        System.out.println("False Positives:          " + summary.getFalsePositives());
        System.out.println("False Negatives:          " + summary.getFalseNegatives());
        System.out.println("----------------------------------------");
        System.out.printf("Precision:                %.4f%n", summary.getPrecision());
        System.out.printf("Recall:                   %.4f%n", summary.getRecall());
        System.out.printf("F1-Score:                 %.4f%n", summary.getF1Score());
        System.out.printf("Accuracy:                 %.4f%n", summary.getAccuracy());
        System.out.println("----------------------------------------");
        System.out.printf("Avg Processing Time:      %.2f ms%n", summary.getAverageProcessingTimeMs());
        System.out.println("========================================");
    }
    
    /**
     * Summary of all calculated metrics.
     */
    @Data
    @lombok.Builder
    public static class MetricsSummary {
        private int truePositives;
        private int trueNegatives;
        private int falsePositives;
        private int falseNegatives;
        private double precision;
        private double recall;
        private double f1Score;
        private double accuracy;
        private int totalTests;
        private double averageProcessingTimeMs;
    }
}

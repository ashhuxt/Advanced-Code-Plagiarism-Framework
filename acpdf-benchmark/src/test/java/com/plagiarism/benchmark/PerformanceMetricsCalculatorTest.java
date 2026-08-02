package com.plagiarism.benchmark;

import com.plagiarism.benchmark.metrics.PerformanceMetricsCalculator;
import com.plagiarism.benchmark.model.BenchmarkResult;
import com.plagiarism.benchmark.model.CodePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PerformanceMetricsCalculator.
 */
class PerformanceMetricsCalculatorTest {
    
    private PerformanceMetricsCalculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new PerformanceMetricsCalculator();
    }
    
    @Test
    void testCalculateMetricsWithPerfectDetection() {
        // Add true positive
        testResults.add(createResult(true, "PLAGIARISM", "PLAGIARISM"));
        // Add true negative
        testResults.add(createResult(false, "NO_PLAGIARISM", "NO_PLAGIARISM"));
        
        PerformanceMetricsCalculator.MetricsSummary summary = 
            calculator.calculateMetrics(testResults);
        
        assertEquals(1, summary.getTruePositives());
        assertEquals(1, summary.getTrueNegatives());
        assertEquals(0, summary.getFalsePositives());
        assertEquals(0, summary.getFalseNegatives());
        assertEquals(1.0, summary.getPrecision());
        assertEquals(1.0, summary.getRecall());
        assertEquals(1.0, summary.getF1Score());
        assertEquals(1.0, summary.getAccuracy());
    }
    
    @Test
    void testCalculateMetricsWithFalsePositives() {
        // Add false positive
        testResults.add(createResult(false, "NO_PLAGIARISM", "PLAGIARISM"));
        // Add true negative
        testResults.add(createResult(false, "NO_PLAGIARISM", "NO_PLAGIARISM"));
        
        PerformanceMetricsCalculator.MetricsSummary summary = 
            calculator.calculateMetrics(testResults);
        
        assertEquals(0, summary.getTruePositives());
        assertEquals(1, summary.getTrueNegatives());
        assertEquals(1, summary.getFalsePositives());
        assertEquals(0, summary.getFalseNegatives());
        assertEquals(0.0, summary.getPrecision());
        assertEquals(0.0, summary.getRecall());
    }
    
    @Test
    void testCalculateMetricsWithFalseNegatives() {
        // Add false negative
        testResults.add(createResult(true, "PLAGIARISM", "NO_PLAGIARISM"));
        // Add true positive
        testResults.add(createResult(true, "PLAGIARISM", "PLAGIARISM"));
        
        PerformanceMetricsCalculator.MetricsSummary summary = 
            calculator.calculateMetrics(testResults);
        
        assertEquals(1, summary.getTruePositives());
        assertEquals(0, summary.getTrueNegatives());
        assertEquals(0, summary.getFalsePositives());
        assertEquals(1, summary.getFalseNegatives());
        assertEquals(1.0, summary.getPrecision());
        assertEquals(0.5, summary.getRecall());
        assertTrue(summary.getF1Score() > 0.6 && summary.getF1Score() < 0.7);
    }
    
    @Test
    void testCalculateMetricsWithEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMetrics(new ArrayList<>());
        });
    }
    
    @Test
    void testCalculateMetricsWithNullList() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMetrics(null);
        });
    }
    
    @Test
    void testPrintReport() {
        testResults.add(createResult(true, "PLAGIARISM", "PLAGIARISM"));
        
        PerformanceMetricsCalculator.MetricsSummary summary = 
            calculator.calculateMetrics(testResults);
        
        // Should not throw exception
        assertDoesNotThrow(() -> calculator.printReport(summary));
    }
    
    private List<BenchmarkResult> testResults = new ArrayList<>();
    
    private BenchmarkResult createResult(boolean isPositive, String expectedVerdict, String actualVerdict) {
        CodePair pair = CodePair.builder()
                .id("test_pair")
                .sourceCode("test code")
                .targetCode("test code")
                .expectedVerdict(expectedVerdict)
                .build();
        
        return BenchmarkResult.builder()
                .codePair(pair)
                .actualSimilarity(isPositive ? 80.0 : 10.0)
                .actualVerdict(actualVerdict)
                .correctDetection(expectedVerdict.equalsIgnoreCase(actualVerdict))
                .processingTimeMs(100)
                .build();
    }
}

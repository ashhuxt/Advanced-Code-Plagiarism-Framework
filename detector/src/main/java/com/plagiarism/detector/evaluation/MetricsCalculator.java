package com.plagiarism.detector.evaluation;

import com.plagiarism.detector.dto.ComparisonResult;
import java.util.List;

public class MetricsCalculator {
    public static MetricsResult calculate(List<ComparisonResult> results, double threshold) {
        int tp = 0, fp = 0, fn = 0;
        for (ComparisonResult res : results) {
            boolean predicted = res.getHybridScore() >= threshold;
            boolean actual = res.getGroundTruth() == 1;

            if (predicted && actual) tp++;
            else if (predicted && !actual) fp++;
            else if (!predicted && actual) fn++;
        }
        double precision = (tp + fp == 0) ? 0 : (double) tp / (tp + fp);
        double recall = (tp + fn == 0) ? 0 : (double) tp / (tp + fn);
        double f1 = (precision + recall == 0) ? 0 : 2 * (precision * recall) / (precision + recall);
        return new MetricsResult(precision, recall, f1);
    }
}
package com.plagiarism.detector.statistics;

import com.plagiarism.detector.dto.ComparisonResult;
import java.util.List;

public class McNemarTest {
    // b = Model A correct, Model B incorrect | c = Model A incorrect, Model B correct
    public static double calculateMcNemar(List<ComparisonResult> results, double threshold) {
        int b = 0, c = 0;
        for (ComparisonResult res : results) {
            boolean hybridCorrect = (res.getHybridScore() >= threshold) == (res.getGroundTruth() == 1);
            boolean baselineCorrect = (res.getTfidfScore() >= threshold) == (res.getGroundTruth() == 1);

            if (hybridCorrect && !baselineCorrect) b++;
            if (!hybridCorrect && baselineCorrect) c++;
        }
        // Chi-square formula: (b-c)^2 / (b+c)
        return (double) Math.pow(Math.abs(b - c) - 1, 2) / (b + c);
    }
}
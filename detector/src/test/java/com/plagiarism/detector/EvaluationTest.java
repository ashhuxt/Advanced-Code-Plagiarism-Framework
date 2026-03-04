package com.plagiarism.detector;

import com.plagiarism.detector.model.EvaluationResult;
import com.plagiarism.detector.util.EvaluationMetrics;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EvaluationTest {

    @Test
    public void testPlagiarismDetectionMetrics() {
        // Mock data: similarity scores and actual ground truth labels (1 = Plagiarized, 0 = Original)
        List<Double> predicted = List.of(85.0, 20.0, 78.0, 15.0);
        List<Integer> actual = List.of(1, 0, 1, 0);

        // Evaluate at a 70.0% threshold
        EvaluationResult result = EvaluationMetrics.evaluate(predicted, actual, 70.0);

        // Print results to the console to verify logic
        System.out.println("Precision: " + result.getPrecision());
        System.out.println("Recall: " + result.getRecall());
        System.out.println("F1 Score: " + result.getF1Score());
    }
}
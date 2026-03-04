package com.plagiarism.detector.util;

import com.plagiarism.detector.model.EvaluationResult;

import java.util.List;

public class EvaluationMetrics {

    public static EvaluationResult evaluate(
            List<Double> predictedScores,
            List<Integer> actualLabels,
            double threshold) {

        int tp = 0, fp = 0, tn = 0, fn = 0;

        for (int i = 0; i < predictedScores.size(); i++) {

            int predicted = predictedScores.get(i) >= threshold ? 1 : 0;
            int actual = actualLabels.get(i);

            if (predicted == 1 && actual == 1) tp++;
            else if (predicted == 1 && actual == 0) fp++;
            else if (predicted == 0 && actual == 0) tn++;
            else if (predicted == 0 && actual == 1) fn++;
        }

        double precision = tp + fp == 0 ? 0 : (double) tp / (tp + fp);
        double recall = tp + fn == 0 ? 0 : (double) tp / (tp + fn);
        double f1 = precision + recall == 0 ? 0 :
                2 * (precision * recall) / (precision + recall);

        return new EvaluationResult(tp, fp, tn, fn,
                precision, recall, f1);
    }
}
package com.plagiarism.detector.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsResult {
    private double precision;
    private double recall;
    private double f1Score;
}
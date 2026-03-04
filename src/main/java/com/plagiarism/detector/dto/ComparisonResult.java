package com.plagiarism.detector.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ComparisonResult {
    private String fileA;
    private String fileB;
    private double astScore;
    private double lcsScore;
    private double tfidfScore;
    private double hybridScore;
    private int groundTruth; // 1 if actually plagiarized, 0 if clean
}
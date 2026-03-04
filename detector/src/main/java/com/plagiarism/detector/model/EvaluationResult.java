package com.plagiarism.detector.model;


public class EvaluationResult {

    private int truePositive;
    private int falsePositive;
    private int trueNegative;
    private int falseNegative;

    private double precision;
    private double recall;
    private double f1Score;

    public EvaluationResult(int tp, int fp, int tn, int fn,
                            double precision,
                            double recall,
                            double f1Score) {

        this.truePositive = tp;
        this.falsePositive = fp;
        this.trueNegative = tn;
        this.falseNegative = fn;
        this.precision = precision;
        this.recall = recall;
        this.f1Score = f1Score;
    }

    // Public Getters to allow access from testing classes
    public int getTruePositive() { return truePositive; }
    public int getFalsePositive() { return falsePositive; }
    public int getTrueNegative() { return trueNegative; }
    public int getFalseNegative() { return falseNegative; }

    public double getPrecision() { return precision; }
    public double getRecall() { return recall; }
    public double getF1Score() { return f1Score; }
}
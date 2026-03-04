package com.plagiarism.detector.experiment;

import com.plagiarism.detector.dto.ComparisonResult;
import com.plagiarism.detector.evaluation.MetricsCalculator;
import com.plagiarism.detector.evaluation.MetricsResult;
import com.plagiarism.detector.statistics.McNemarTest;
import com.plagiarism.detector.util.ResultsExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    @Autowired
    private AutomatedExperimentRunner runner;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" Starting Research Experimentation Framework...");

        // 1. Run full pairwise comparison
        List<ComparisonResult> results = runner.runFullExperiment("src/main/resources/dataset/");

        // 2. Perform Threshold Loop Analysis (Phase 3)
        System.out.println("\n--- Threshold Analysis (Precision-Recall Optimization) ---");
        System.out.println("Threshold\tF1-Score");
        for (double t = 0.1; t < 1.0; t += 0.1) {
            MetricsResult m = MetricsCalculator.calculate(results, t);
            System.out.printf("%.1f\t\t%.4f%n", t, m.getF1Score());
        }

        // 3. Statistical Validation (Phase 4 - McNemar's Test)
        double pValue = McNemarTest.calculateMcNemar(results, 0.7);
        System.out.println("\n--- Statistical Validation (McNemar's Test) ---");
        System.out.println("McNemar Chi-Square Value: " + String.format("%.4f", pValue));
        if (pValue > 3.84) {
            System.out.println("Result: Improvement is statistically significant (p < 0.05).");
        } else {
            System.out.println("Result: Improvement is not statistically significant.");
        }

        // 4. Export to CSV for visual analysis
        try {
            ResultsExporter.exportToCSV(results, "experiment_results.csv");
            System.out.println("\nResults successfully exported to experiment_results.csv");
        } catch (IOException e) {
            System.err.println(" Failed to export results: " + e.getMessage());
        }

        System.out.println("\nResearch framework complete.");
    }
}
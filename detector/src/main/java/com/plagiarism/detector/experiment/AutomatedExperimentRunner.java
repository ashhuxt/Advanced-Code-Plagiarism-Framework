package com.plagiarism.detector.experiment;

import com.plagiarism.detector.dto.ComparisonResult;
import com.plagiarism.detector.service.SimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AutomatedExperimentRunner {

    @Autowired
    private SimilarityService similarityService;

    public List<ComparisonResult> runFullExperiment(String datasetPath) throws IOException {
        // Now it uses the static methods from the ExperimentUtils class
        Map<String, String> files = ExperimentUtils.loadFiles(datasetPath);
        List<String[]> pairs = ExperimentUtils.generatePairs(files.keySet());
        List<ComparisonResult> results = new ArrayList<>();

        for (String[] pair : pairs) {
            String codeA = files.get(pair[0]);
            String codeB = files.get(pair[1]);

            double ast = similarityService.calculateASTSimilarity(codeA, codeB);
            double lcs = similarityService.calculateLCSSimilarity(codeA, codeB);
            double tfidf = similarityService.calculateTFIDFSimilarity(codeA, codeB);
            double hybrid = (ast + lcs + tfidf) / 3.0;

            int label = pair[0].split("_")[0].equals(pair[1].split("_")[0]) ? 1 : 0;

            results.add(new ComparisonResult(pair[0], pair[1], ast, lcs, tfidf, hybrid, label));
        }
        return results;
    }
}
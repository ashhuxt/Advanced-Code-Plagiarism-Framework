package com.plagiarism.detector.util;

import com.plagiarism.detector.dto.ComparisonResult;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResultsExporter {
    public static void exportToCSV(List<ComparisonResult> results, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("FileA,FileB,AST,LCS,TFIDF,Hybrid,GroundTruth");
            for (ComparisonResult res : results) {
                writer.printf("%s,%s,%.4f,%.4f,%.4f,%.4f,%d%n",
                        res.getFileA(), res.getFileB(),
                        res.getAstScore(), res.getLcsScore(),
                        res.getTfidfScore(), res.getHybridScore(), res.getGroundTruth());
            }
        }
    }
}
package com.plagiarism.detector;

import com.plagiarism.detector.service.SimilarityService; // Import your existing service
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest // Needed to inject your Spring-managed SimilarityService
public class MutationTest {

    @Autowired
    private SimilarityService similarityService;

    @Test
    public void runRobustnessExperiment() throws Exception {
        MutatorEngine engine = new MutatorEngine();
        Path datasetDir = Path.of("src/main/resources/dataset");
        Path outputDir = Path.of("src/test/resources/mutants");

        Files.createDirectories(outputDir);

        System.out.println("FILE NAME | SIMILARITY SCORE (%)");
        System.out.println("---------------------------------");

        Files.list(datasetDir)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String fileName = path.getFileName().toString();
                        String outPath = outputDir.resolve(fileName).toString();

                        // 1. Generate Mutant
                        engine.generateVariableMutant(path.toString(), outPath);

                        // 2. Compare Original vs Mutant
                        // Change this line:
                        double score = similarityService.calculateHybridSimilarity(
                                Files.readString(path),
                                Files.readString(Path.of(outPath))
                        );

                        System.out.printf("%-10s | %.2f%%%n", fileName, score * 100);

                    } catch (Exception e) {
                        System.err.println("Error processing: " + path.getFileName());
                    }
                });
    }
}
package com.plagiarism.detector;

import com.plagiarism.detector.model.CodeFile;
import com.plagiarism.detector.model.EvaluationResult;
import com.plagiarism.detector.repository.CodeFileRepository;
import com.plagiarism.detector.service.SimilarityService;
import com.plagiarism.detector.util.EvaluationMetrics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URL;
import java.util.*;

@SpringBootTest // This annotation is necessary to load the Spring context in tests
public class ExperimentRunner {

    @Autowired
    private SimilarityService similarityService;

    @Autowired
    private CodeFileRepository repository;

    @Test // Adding this allows you to click the green 'Play' icon in your IDE
    public void runComparisonExperiment() {
        List<File> files = loadDatasetFiles();
        List<Integer> actualLabels = new ArrayList<>();

        List<Double> astScores = new ArrayList<>(), lcsScores = new ArrayList<>(),
                tfidfScores = new ArrayList<>(), hybridScores = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {
                Long id1 = resolveId(files.get(i));
                Long id2 = resolveId(files.get(j));

                if (id1 == -1L || id2 == -1L) continue;

                actualLabels.add(isPlagiarism(files.get(i), files.get(j)) ? 1 : 0);

                astScores.add(similarityService.compareASTOnly(id1, id2));
                lcsScores.add(similarityService.compareLCSOnly(id1, id2));
                tfidfScores.add(similarityService.compareTFIDFOnly(id1, id2));
                hybridScores.add(similarityService.compareHybrid(id1, id2));
            }
        }

        printResult("AST Model", EvaluationMetrics.evaluate(astScores, actualLabels, 0.7));
        printResult("LCS Model", EvaluationMetrics.evaluate(lcsScores, actualLabels, 0.7));
        printResult("TF-IDF Model", EvaluationMetrics.evaluate(tfidfScores, actualLabels, 0.7));
        printResult("Hybrid Model", EvaluationMetrics.evaluate(hybridScores, actualLabels, 0.7));
    }

    public List<File> loadDatasetFiles() {
        List<File> files = new ArrayList<>();
        URL resource = getClass().getClassLoader().getResource("dataset");
        if (resource != null) {
            File folder = new File(resource.getFile());
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                files.addAll(Arrays.asList(listOfFiles));
            }
        }
        return files;
    }

    private Long resolveId(File f) {
        CodeFile file = repository.findByName(f.getName());
        return (file != null) ? file.getId() : -1L;
    }

    private boolean isPlagiarism(File f1, File f2) {
        String n1 = f1.getName().replaceAll("\\.java", "");
        String n2 = f2.getName().replaceAll("\\.java", "");
        if ((n1.startsWith("O") && n2.startsWith("P")) || (n1.startsWith("P") && n2.startsWith("O"))) {
            return n1.substring(1).equals(n2.substring(1));
        }
        return false;
    }

    private void printResult(String name, EvaluationResult res) {
        System.out.printf("%s | Precision: %.2f | Recall: %.2f | F1: %.2f%n",
                name, res.getPrecision(), res.getRecall(), res.getF1Score());
    }
}
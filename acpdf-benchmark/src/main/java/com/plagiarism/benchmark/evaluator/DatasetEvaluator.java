package com.plagiarism.benchmark.evaluator;

import com.plagiarism.benchmark.model.CodePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for loading benchmark datasets from directories or mock data.
 * Supports reading code pairs from structured directory layouts.
 */
@Slf4j
@Component
public class DatasetEvaluator {
    
    private static final String DEFAULT_SIMILARITY_THRESHOLD = "50.0";
    
    /**
     * Load code pairs from a directory structure.
     * Expected format:
     * dataset/
     *   pair1/
     *     source.java
     *     target.java
     *     metadata.properties (optional, contains expectedVerdict, expectedSimilarity)
     */
    public List<CodePair> loadFromDirectory(String datasetPath) throws IOException {
        List<CodePair> codePairs = new ArrayList<>();
        Path datasetRoot = Paths.get(datasetPath);
        
        if (!Files.exists(datasetRoot)) {
            throw new IllegalArgumentException("Dataset path does not exist: " + datasetPath);
        }
        
        Files.walkFileTree(datasetRoot, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.getFileName().toString().equals("metadata.properties")) {
                    try {
                        CodePair pair = parseCodePair(file.getParent());
                        if (pair != null) {
                            codePairs.add(pair);
                        }
                    } catch (IOException e) {
                        log.error("Failed to parse code pair at {}", file.getParent(), e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        
        log.info("Loaded {} code pairs from dataset: {}", codePairs.size(), datasetPath);
        return codePairs;
    }
    
    /**
     * Parse a code pair from a directory containing source.java, target.java, and metadata.properties
     */
    private CodePair parseCodePair(Path pairDir) throws IOException {
        Path sourceFile = pairDir.resolve("source.java");
        Path targetFile = pairDir.resolve("target.java");
        Path metadataFile = pairDir.resolve("metadata.properties");
        
        if (!Files.exists(sourceFile) || !Files.exists(targetFile)) {
            log.warn("Skipping incomplete pair at {} - missing source or target file", pairDir);
            return null;
        }
        
        String sourceCode = Files.readString(sourceFile);
        String targetCode = Files.readString(targetFile);
        
        // Parse metadata if exists, otherwise use defaults
        String expectedVerdict = "NO_PLAGIARISM";
        double expectedSimilarity = 0.0;
        
        if (Files.exists(metadataFile)) {
            List<String> lines = Files.readAllLines(metadataFile);
            for (String line : lines) {
                if (line.startsWith("expectedVerdict=")) {
                    expectedVerdict = line.substring("expectedVerdict=".length()).trim();
                } else if (line.startsWith("expectedSimilarity=")) {
                    try {
                        expectedSimilarity = Double.parseDouble(
                            line.substring("expectedSimilarity=".length()).trim());
                    } catch (NumberFormatException e) {
                        log.warn("Invalid expectedSimilarity value, using default 0.0");
                    }
                }
            }
        }
        
        return CodePair.builder()
                .id(pairDir.getFileName().toString())
                .sourceCode(sourceCode)
                .targetCode(targetCode)
                .expectedVerdict(expectedVerdict)
                .expectedSimilarity(expectedSimilarity)
                .sourceFileName(sourceFile.getFileName().toString())
                .targetFileName(targetFile.getFileName().toString())
                .metadata(pairDir.toString())
                .build();
    }
    
    /**
     * Create mock dataset for testing purposes.
     */
    public List<CodePair> createMockDataset() {
        List<CodePair> mockPairs = new ArrayList<>();
        
        // Mock pair 1: Obvious plagiarism
        mockPairs.add(CodePair.builder()
                .id("mock_001")
                .sourceCode("public class Test { public static void main(String[] args) { System.out.println(\"Hello\"); } }")
                .targetCode("public class Test { public static void main(String[] args) { System.out.println(\"Hello\"); } }")
                .expectedVerdict("PLAGIARISM")
                .expectedSimilarity(100.0)
                .sourceFileName("Test.java")
                .targetFileName("Original.java")
                .build());
        
        // Mock pair 2: Different code
        mockPairs.add(CodePair.builder()
                .id("mock_002")
                .sourceCode("public class Calculator { public int add(int a, int b) { return a + b; } }")
                .targetCode("public class Printer { public void print(String msg) { System.out.println(msg); } }")
                .expectedVerdict("NO_PLAGIARISM")
                .expectedSimilarity(0.0)
                .sourceFileName("Calculator.java")
                .targetFileName("Printer.java")
                .build());
        
        // Mock pair 3: Partial plagiarism
        mockPairs.add(CodePair.builder()
                .id("mock_003")
                .sourceCode("public class Data { private int value; public int getValue() { return value; } public void setValue(int v) { value = v; } }")
                .targetCode("public class Data { private int value; public int getValue() { return value; } }")
                .expectedVerdict("PLAGIARISM")
                .expectedSimilarity(75.0)
                .sourceFileName("Data.java")
                .targetFileName("DataPartial.java")
                .build());
        
        log.info("Created mock dataset with {} code pairs", mockPairs.size());
        return mockPairs;
    }
    
    /**
     * Evaluate a single code pair through the pipeline.
     * This method is called by benchmark runners.
     */
    public CodePair createCodePair(String id, String sourceCode, String targetCode, 
                                   String expectedVerdict, double expectedSimilarity) {
        return CodePair.builder()
                .id(id)
                .sourceCode(sourceCode)
                .targetCode(targetCode)
                .expectedVerdict(expectedVerdict)
                .expectedSimilarity(expectedSimilarity)
                .build();
    }
}

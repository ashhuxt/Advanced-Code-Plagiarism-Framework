package com.plagiarism.benchmark;

import com.plagiarism.benchmark.evaluator.DatasetEvaluator;
import com.plagiarism.benchmark.model.CodePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DatasetEvaluator.
 */
class DatasetEvaluatorTest {
    
    private DatasetEvaluator datasetEvaluator;
    
    @BeforeEach
    void setUp() {
        datasetEvaluator = new DatasetEvaluator();
    }
    
    @Test
    void testCreateMockDataset() {
        List<CodePair> mockPairs = datasetEvaluator.createMockDataset();
        
        assertNotNull(mockPairs);
        assertEquals(3, mockPairs.size());
        
        // Verify first pair (obvious plagiarism)
        CodePair pair1 = mockPairs.get(0);
        assertEquals("mock_001", pair1.getId());
        assertEquals("PLAGIARISM", pair1.getExpectedVerdict());
        assertEquals(100.0, pair1.getExpectedSimilarity());
        
        // Verify second pair (different code)
        CodePair pair2 = mockPairs.get(1);
        assertEquals("mock_002", pair2.getId());
        assertEquals("NO_PLAGIARISM", pair2.getExpectedVerdict());
        assertEquals(0.0, pair2.getExpectedSimilarity());
        
        // Verify third pair (partial plagiarism)
        CodePair pair3 = mockPairs.get(2);
        assertEquals("mock_003", pair3.getId());
        assertEquals("PLAGIARISM", pair3.getExpectedVerdict());
        assertEquals(75.0, pair3.getExpectedSimilarity());
    }
    
    @Test
    void testCreateCodePair() {
        CodePair pair = datasetEvaluator.createCodePair(
            "test_001",
            "public class Test {}",
            "public class Test {}",
            "PLAGIARISM",
            100.0
        );
        
        assertNotNull(pair);
        assertEquals("test_001", pair.getId());
        assertEquals("public class Test {}", pair.getSourceCode());
        assertEquals("public class Test {}", pair.getTargetCode());
        assertEquals("PLAGIARISM", pair.getExpectedVerdict());
        assertEquals(100.0, pair.getExpectedSimilarity());
    }
    
    @Test
    void testLoadFromDirectoryWithInvalidPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            datasetEvaluator.loadFromDirectory("/nonexistent/path");
        });
    }
    
    @Test
    void testLoadFromDirectoryWithValidStructure() throws IOException {
        // Create temporary directory structure
        Path tempDir = Files.createTempDirectory("benchmark_test");
        Path pairDir = Files.createDirectories(tempDir.resolve("pair1"));
        
        Files.writeString(pairDir.resolve("source.java"), "public class Source {}");
        Files.writeString(pairDir.resolve("target.java"), "public class Target {}");
        Files.writeString(pairDir.resolve("metadata.properties"), 
            "expectedVerdict=NO_PLAGIARISM\nexpectedSimilarity=0.0");
        
        List<CodePair> pairs = datasetEvaluator.loadFromDirectory(tempDir.toString());
        
        assertEquals(1, pairs.size());
        CodePair pair = pairs.get(0);
        assertEquals("pair1", pair.getId());
        assertEquals("public class Source {}", pair.getSourceCode());
        assertEquals("public class Target {}", pair.getTargetCode());
        assertEquals("NO_PLAGIARISM", pair.getExpectedVerdict());
        
        // Cleanup
        Files.deleteIfExists(pairDir.resolve("source.java"));
        Files.deleteIfExists(pairDir.resolve("target.java"));
        Files.deleteIfExists(pairDir.resolve("metadata.properties"));
        Files.deleteIfExists(pairDir);
        Files.deleteIfExists(tempDir);
    }
}

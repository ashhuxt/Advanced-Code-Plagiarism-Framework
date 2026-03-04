package com.plagiarism.detector.experiment;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExperimentUtils {
    public static Map<String, String> loadFiles(String directoryPath) throws IOException {
        return Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .collect(Collectors.toMap(
                        p -> p.getFileName().toString(),
                        p -> {
                            try { return Files.readString(p); }
                            catch (IOException e) { return ""; }
                        }
                ));
    }

    public static List<String[]> generatePairs(Set<String> fileNames) {
        List<String> list = new ArrayList<>(fileNames);
        List<String[]> pairs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                pairs.add(new String[]{list.get(i), list.get(j)});
            }
        }
        return pairs;
    }
}
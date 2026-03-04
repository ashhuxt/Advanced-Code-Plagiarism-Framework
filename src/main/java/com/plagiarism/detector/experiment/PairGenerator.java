package com.plagiarism.detector.experiment;

import java.util.*;

public class PairGenerator {
    public static List<String[]> generatePairs(Set<String> fileNames) {
        List<String> list = new ArrayList<>(fileNames);
        List<String[]> pairs = new ArrayList<>();
        // O(n^2) logic: j starts at i+1 to ensure unique pairs
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                pairs.add(new String[]{list.get(i), list.get(j)});
            }
        }
        return pairs;
    }
}
package com.plagiarism.detector.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityCalculator {

    // Calculates the structural similarity using the LCS algorithm (Weight 0.4).
    public static double calculateSimilarity(List<String> list1, List<String> list2) {
        if (list1 == null || list2 == null || (list1.isEmpty() && list2.isEmpty())) {
            return 0.0;
        }
        return LCSCalculator.calculateSimilarity(list1, list2);
    }

    // Calculates the frequency-based similarity (Weight 0.3).
    public static double calculateFrequencySimilarity(List<String> list1, List<String> list2) {
        if (list1 == null || list2 == null || (list1.isEmpty() && list2.isEmpty())) {
            return 0.0;
        }

        Map<String, Integer> freq1 = getFrequencyMap(list1);
        Map<String, Integer> freq2 = getFrequencyMap(list2);

        int matchCount = 0;
        int totalCount = 0;

        for (String key : freq1.keySet()) {
            int count1 = freq1.getOrDefault(key, 0);
            int count2 = freq2.getOrDefault(key, 0);
            matchCount += Math.min(count1, count2);
            totalCount += Math.max(count1, count2);
        }

        for (String key : freq2.keySet()) {
            if (!freq1.containsKey(key)) {
                totalCount += freq2.get(key);
            }
        }

        return (totalCount == 0) ? 0.0 : (matchCount * 100.0) / totalCount;
    }

    private static Map<String, Integer> getFrequencyMap(List<String> list) {
        Map<String, Integer> map = new HashMap<>();
        for (String item : list) {
            map.put(item, map.getOrDefault(item, 0) + 1);
        }
        return map;
    }
}
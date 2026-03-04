package com.plagiarism.detector.util;

import java.util.*;

public class TFIDFCalculator {

    public static Map<String, Double> computeTF(List<String> tokens) {

        Map<String, Double> tf = new HashMap<>();
        int totalTerms = tokens.size();

        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0.0) + 1.0);
        }

        for (String token : tf.keySet()) {
            tf.put(token, tf.get(token) / totalTerms);
        }

        return tf;
    }

    public static Map<String, Double> computeIDF(List<List<String>> allDocs) {

        Map<String, Double> idf = new HashMap<>();
        int totalDocs = allDocs.size();

        Set<String> uniqueTokens = new HashSet<>();
        for (List<String> doc : allDocs) {
            uniqueTokens.addAll(doc);
        }

        for (String token : uniqueTokens) {

            int docCount = 0;

            for (List<String> doc : allDocs) {
                if (doc.contains(token)) {
                    docCount++;
                }
            }

            idf.put(token, Math.log((double) totalDocs / (docCount + 1)));
        }

        return idf;
    }

    public static Map<String, Double> computeTFIDF(
            Map<String, Double> tf,
            Map<String, Double> idf) {

        Map<String, Double> tfidf = new HashMap<>();

        for (String token : tf.keySet()) {
            tfidf.put(token, tf.get(token) * idf.getOrDefault(token, 0.0));
        }

        return tfidf;
    }

    public static double cosineSimilarity(
            Map<String, Double> vec1,
            Map<String, Double> vec2) {

        Set<String> allTokens = new HashSet<>();
        allTokens.addAll(vec1.keySet());
        allTokens.addAll(vec2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String token : allTokens) {

            double v1 = vec1.getOrDefault(token, 0.0);
            double v2 = vec2.getOrDefault(token, 0.0);

            dotProduct += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }

        if (norm1 == 0 || norm2 == 0) return 0.0;

        return (dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2))) * 100.0;
    }
}
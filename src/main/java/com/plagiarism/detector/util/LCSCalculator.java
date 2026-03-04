package com.plagiarism.detector.util;

import java.util.List;

public class LCSCalculator {

    public static int calculateLCS(List<String> a, List<String> b) {

        int m = a.size();
        int n = b.size();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                if (a.get(i - 1).equals(b.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    public static double calculateSimilarity(List<String> a, List<String> b) {

        int lcs = calculateLCS(a, b);

        return (2.0 * lcs) / (a.size() + b.size()) * 100.0;
    }
}
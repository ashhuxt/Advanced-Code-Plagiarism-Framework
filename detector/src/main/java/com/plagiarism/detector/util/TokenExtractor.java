package com.plagiarism.detector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TokenExtractor {

    public static List<String> extractTokens(String code) {

        List<String> tokens = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(
                code,
                " \n\t{}();,+-*/=<>&|![]",
                false
        );

        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }

        return tokens;
    }
}
package com.plagiarism.detector.util;

import java.io.*;
import java.util.*;

public class GroundTruthGenerator {

    public static void main(String[] args) throws Exception {
        List<String> files = new ArrayList<>();

        // Add originals, plagiarized, and unrelated
        for(int i=1; i<=10; i++) files.add("O" + i);
        for(int i=1; i<=10; i++) files.add("P" + i);
        for(int i=1; i<=10; i++) files.add("U" + i);

        PrintWriter writer = new PrintWriter("ground_truth.csv");
        writer.println("file1,file2,label");

        for(int i=0; i<files.size(); i++){
            for(int j=i+1; j<files.size(); j++){
                String f1 = files.get(i);
                String f2 = files.get(j);
                int label = isPlagiarismPair(f1, f2) ? 1 : 0;
                writer.println(f1 + "," + f2 + "," + label);
            }
        }
        writer.close();
        System.out.println("Ground truth generated successfully.");
    }

    private static boolean isPlagiarismPair(String a, String b){
        // Logic for O and P pairs
        if ((a.startsWith("O") && b.startsWith("P")) || (a.startsWith("P") && b.startsWith("O"))) {
            return a.substring(1).equals(b.substring(1));
        }
        return false;
    }
}
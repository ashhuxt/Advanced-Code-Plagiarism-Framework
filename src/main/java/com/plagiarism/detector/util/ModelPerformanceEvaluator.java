package com.plagiarism.detector.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ModelPerformanceEvaluator {
    public static void main(String[] args) {
        // Updated path: Ensure this points to your specific project location
        File file = new File("C:\\Users\\Asus\\Downloads\\detector\\detector\\experiment_results.csv");

        if (!file.exists()) {
            System.err.println("Error: Could not find file at " + file.getAbsolutePath());
            return;
        }

        double sumAST = 0, sumLCS = 0, sumTFIDF = 0, sumHybrid = 0;
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 6) {
                    sumAST += Double.parseDouble(data[2]);
                    sumLCS += Double.parseDouble(data[3]);
                    sumTFIDF += Double.parseDouble(data[4]);
                    sumHybrid += Double.parseDouble(data[5]);
                    count++;
                }
            }

            if (count > 0) {
                double avgAST = sumAST / count;
                double avgLCS = sumLCS / count;
                double avgTFIDF = sumTFIDF / count;
                double avgHybrid = sumHybrid / count;

                System.out.println("--- Model Performance Summary ---");
                System.out.println("Processed " + count + " records.");

                System.out.println("\n--- Performance Visualization ---");
                printBar("AST", avgAST);
                printBar("LCS", avgLCS);
                printBar("TF-IDF", avgTFIDF);
                printBar("Hybrid", avgHybrid);

            } else {
                System.out.println("No data found to process.");
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printBar(String label, double value) {
        // Creates a simple bar chart in the console
        int barLength = (int) (value / 2);
        System.out.printf("%-7s | %-50s %.2f%n", label, "█".repeat(barLength), value);
    }
}
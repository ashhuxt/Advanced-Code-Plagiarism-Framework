package com.plagiarism.detector.model;

import jakarta.persistence.*;

@Entity
public class SimilarityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long file1Id;
    private Long file2Id;

    // New fields to store file names for better readability
    private String file1Name;
    private String file2Name;

    private double similarityScore;

    public SimilarityResult() {}

    // Updated constructor to include file names
    public SimilarityResult(Long file1Id, Long file2Id, String file1Name, String file2Name, double similarityScore) {
        this.file1Id = file1Id;
        this.file2Id = file2Id;
        this.file1Name = file1Name;
        this.file2Name = file2Name;
        this.similarityScore = similarityScore;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Long getFile1Id() { return file1Id; }
    public Long getFile2Id() { return file2Id; }
    public String getFile1Name() { return file1Name; }
    public String getFile2Name() { return file2Name; }
    public double getSimilarityScore() { return similarityScore; }
}
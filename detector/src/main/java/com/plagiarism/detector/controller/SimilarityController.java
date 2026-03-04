package com.plagiarism.detector.controller;

import com.plagiarism.detector.model.SimilarityResult;
import com.plagiarism.detector.service.SimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/similarity")
public class SimilarityController {

    @Autowired
    private SimilarityService similarityService;

    // Returns a formatted String for the UI/Browser
    @GetMapping("/compare")
    public ResponseEntity<String> compare(@RequestParam Long file1, @RequestParam Long file2) {
        double similarity = similarityService.compareFiles(file1, file2);
        return ResponseEntity.ok("Similarity Score: " + String.format("%.1f", similarity) + "%");
    }

    // Triggers the batch job
    @PostMapping("/compare-all")
    public ResponseEntity<String> compareAll() {
        similarityService.compareAllFiles();
        return ResponseEntity.ok("All files have been compared and results stored.");
    }

    // Fetches results above a specific threshold
    @GetMapping("/suspicious")
    public ResponseEntity<List<SimilarityResult>> getSuspicious(@RequestParam double threshold) {
        List<SimilarityResult> results = similarityService.getSuspiciousPairs(threshold);
        return ResponseEntity.ok(results);
    }
}
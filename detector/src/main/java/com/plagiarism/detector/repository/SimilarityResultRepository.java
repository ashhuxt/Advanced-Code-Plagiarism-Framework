package com.plagiarism.detector.repository;

import com.plagiarism.detector.model.SimilarityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SimilarityResultRepository extends JpaRepository<SimilarityResult, Long> {
    // Spring Data JPA uses the field name.
    // If your model field is 'similarityScore', this is the correct signature:
    List<SimilarityResult> findBySimilarityScoreGreaterThan(double threshold);
}
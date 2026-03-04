package com.plagiarism.detector.repository;

import com.plagiarism.detector.model.CodeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeFileRepository extends JpaRepository<CodeFile, Long> {
    // Spring Data JPA automatically implements this based on the method name
    CodeFile findByName(String name);
}
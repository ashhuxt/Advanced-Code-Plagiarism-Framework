package com.plagiarism.detector.service;

import com.plagiarism.detector.model.CodeFile;
import com.plagiarism.detector.repository.CodeFileRepository;
import com.plagiarism.detector.util.ASTGenerator;
import com.github.javaparser.ast.CompilationUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeFileService {

    @Autowired
    private CodeFileRepository repository;

    public CodeFile saveFile(String name, String content) {
        // Generate AST
        CompilationUnit cu = ASTGenerator.generateAST(content);
        System.out.println("AST Generated Successfully for: " + name);

        // Extract and print node types for verification
        var nodes = ASTGenerator.extractNodeTypes(cu);
        nodes.forEach(System.out::println);

        // UPDATED: Use the correct constructor for the 'name' field
        return repository.save(new CodeFile(name, content));
    }

    public List<CodeFile> getAllFiles() {
        return repository.findAll();
    }

    public String compareFiles(Long id1, Long id2) {
        CodeFile file1 = repository.findById(id1).orElseThrow();
        CodeFile file2 = repository.findById(id2).orElseThrow();

        List<String> nodes1 = ASTGenerator.extractNodeTypes(ASTGenerator.generateAST(file1.getCodeContent()));
        List<String> nodes2 = ASTGenerator.extractNodeTypes(ASTGenerator.generateAST(file2.getCodeContent()));

        double similarity = calculateSimilarity(nodes1, nodes2);
        return "Similarity Score: " + String.format("%.1f", similarity) + "%";
    }

    private double calculateSimilarity(List<String> n1, List<String> n2) {
        long intersectionCount = n1.stream().filter(n2::contains).count();
        int unionSize = (int) n1.stream().distinct().count() + (int) n2.stream().distinct().count()
                - (int) intersectionCount;

        if (unionSize == 0) return 0.0; // Prevent division by zero
        return (double) intersectionCount / unionSize * 100;
    }
}
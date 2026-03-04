package com.plagiarism.detector.controller;

import com.plagiarism.detector.model.CodeFile;
import com.plagiarism.detector.service.CodeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class CodeFileController {

    @Autowired
    private CodeFileService service;

    @PostMapping("/upload")
    public CodeFile uploadFile(@RequestBody CodeFile file) {
        // Updated: changed getFileName() to getName()
        return service.saveFile(file.getName(), file.getCodeContent());
    }

    @GetMapping
    public List<CodeFile> getAllFiles() {
        return service.getAllFiles();
    }

    @GetMapping("/similarity")
    public ResponseEntity<String> getSimilarity(@RequestParam Long file1, @RequestParam Long file2) {
        String result = service.compareFiles(file1, file2);
        return ResponseEntity.ok(result);
    }
}
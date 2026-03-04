package com.plagiarism.detector.util;

import com.plagiarism.detector.model.CodeFile;
import com.plagiarism.detector.repository.CodeFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CodeFileRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // Only load if the database is empty
        if (repository.count() == 0) {
            URL resource = getClass().getClassLoader().getResource("dataset");
            if (resource != null) {
                File folder = new File(resource.getFile());
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File file : files) {
                        String content = new String(Files.readAllBytes(file.toPath()));
                        CodeFile codeFile = new CodeFile(file.getName(), content);
                        repository.save(codeFile);
                    }
                    System.out.println("Database seeded with " + files.length + " files.");
                }
            }
        }
    }
}
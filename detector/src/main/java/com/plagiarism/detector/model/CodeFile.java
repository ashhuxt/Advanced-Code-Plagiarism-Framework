package com.plagiarism.detector.model;

import jakarta.persistence.*;

@Entity
@Table(name = "code_file")
public class CodeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Explicitly mapping the column ensures Hibernate connects the 'name' field correctly
    @Column(name = "name")
    private String name;

    @Column(name = "code_content", columnDefinition = "LONGTEXT")
    private String codeContent;

    public CodeFile() {}

    public CodeFile(String name, String codeContent) {
        this.name = name;
        this.codeContent = codeContent;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }
}
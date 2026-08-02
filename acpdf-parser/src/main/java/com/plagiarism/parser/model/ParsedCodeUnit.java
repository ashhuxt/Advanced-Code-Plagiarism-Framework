package com.plagiarism.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parsed structural unit of code in a neutral format.
 * This abstraction allows different language parsers to produce compatible output.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCodeUnit {

    /**
     * Type of the code unit (e.g., FUNCTION, LOOP, CONDITION, VARIABLE, CLASS)
     */
    private UnitType type;

    /**
     * Name or identifier of the code unit
     */
    private String name;

    /**
     * The actual code content/representation of this unit
     */
    private String content;

    /**
     * Starting line number in the original source
     */
    private int startLine;

    /**
     * Ending line number in the original source
     */
    private int endLine;

    /**
     * Child units nested within this unit (for hierarchical structures)
     */
    @Builder.Default
    private List<ParsedCodeUnit> children = new ArrayList<>();

    /**
     * Metadata attributes (e.g., variable types, modifiers, access levels)
     */
    @Builder.Default
    private List<String> metadata = new ArrayList<>();

    /**
     * Types of code units that can be extracted
     */
    public enum UnitType {
        FUNCTION,
        METHOD,
        CLASS,
        INTERFACE,
        LOOP,
        CONDITION,
        VARIABLE,
        STATEMENT,
        EXPRESSION,
        IMPORT,
        PACKAGE,
        COMMENT,
        UNKNOWN
    }

    /**
     * Add a child unit to this unit
     */
    public void addChild(ParsedCodeUnit child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    /**
     * Add metadata attribute
     */
    public void addMetadata(String attribute) {
        if (this.metadata == null) {
            this.metadata = new ArrayList<>();
        }
        this.metadata.add(attribute);
    }
}

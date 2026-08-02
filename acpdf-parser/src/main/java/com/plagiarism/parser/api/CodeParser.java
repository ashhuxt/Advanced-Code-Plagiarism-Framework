package com.plagiarism.parser.api;

import com.plagiarism.parser.model.ParsedCodeUnit;
import java.util.List;

/**
 * Unified interface for parsing source code files into a neutral format.
 * Supports multiple programming languages through concrete implementations.
 */
public interface CodeParser {

    /**
     * Parse source code content into a list of structured code units.
     * 
     * @param sourceCode The raw source code content to parse
     * @return List of parsed code units containing structural components
     * @throws ParseException if parsing fails
     */
    List<ParsedCodeUnit> parse(String sourceCode) throws ParseException;

    /**
     * Get the supported language for this parser.
     * 
     * @return The language name (e.g., "Java", "Python", "JavaScript")
     */
    String getSupportedLanguage();

    /**
     * Check if this parser can handle the given file extension.
     * 
     * @param fileExtension The file extension to check (e.g., ".java", ".py")
     * @return true if the parser supports this extension
     */
    boolean supportsExtension(String fileExtension);

    /**
     * Extract all function/method declarations from the source code.
     * 
     * @param sourceCode The raw source code content
     * @return List of function names found in the code
     * @throws ParseException if extraction fails
     */
    List<String> extractFunctions(String sourceCode) throws ParseException;

    /**
     * Extract all loop structures (for, while, do-while) from the source code.
     * 
     * @param sourceCode The raw source code content
     * @return List of loop representations found in the code
     * @throws ParseException if extraction fails
     */
    List<String> extractLoops(String sourceCode) throws ParseException;

    /**
     * Extract all conditional statements (if, else, switch) from the source code.
     * 
     * @param sourceCode The raw source code content
     * @return List of condition representations found in the code
     * @throws ParseException if extraction fails
     */
    List<String> extractConditions(String sourceCode) throws ParseException;

    /**
     * Extract all variable declarations from the source code.
     * 
     * @param sourceCode The raw source code content
     * @return List of variable names found in the code
     * @throws ParseException if extraction fails
     */
    List<String> extractVariables(String sourceCode) throws ParseException;
}

package com.mutation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.mutation.strategies.VariableRenamer;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MutatorEngine {

    /**
     * Takes a source file, applies variable renaming, and saves the mutant.
     */
    public void generateVariableMutant(String inputPath, String outputPath) throws Exception {
        // 1. Parse the code into an AST
        CompilationUnit cu = StaticJavaParser.parse(new File(inputPath));

        // 2. Apply our custom VariableRenamer visitor
        VariableRenamer renamer = new VariableRenamer();
        renamer.visit(cu, null);

        // 3. Save the mutated source code to the output path
        Files.writeString(Path.of(outputPath), cu.toString());

        System.out.println("Mutation complete: " + outputPath);
    }
}
package pl.edu.pg.examgeneratorng;

import java.nio.file.Path;
import java.nio.file.Paths;

import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;
import static pl.edu.pg.examgeneratorng.LoggingConfig.configureLogging;

public class Main {
    public static void main(String[] args) throws Exception {
        configureLogging();
        Path workspacePath = Paths.get(args[0]);
        generateAllExamVariants(workspacePath, diagnostic ->
                System.err.println(diagnostic.getMessage()));
    }
}

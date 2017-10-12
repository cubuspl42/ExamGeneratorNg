package pl.edu.pg.examgeneratorng;

import java.nio.file.Paths;

import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;

public class Main {
    public static void main(String[] args) throws Exception {
        String workspacePath = args[0];
        generateAllExamVariants(Paths.get(workspacePath));
    }
}

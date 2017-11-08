package pl.edu.pg.examgeneratorng;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger.getLogger("org.odftoolkit.odfdom.pkg.OdfXMLFactory").setLevel(Level.WARNING);
        String workspacePath = args[0];
        generateAllExamVariants(Paths.get(workspacePath));
    }
}

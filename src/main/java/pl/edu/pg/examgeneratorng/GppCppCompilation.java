package pl.edu.pg.examgeneratorng;

import pl.edu.pg.examgeneratorng.util.ProcessUtils;

import java.io.IOException;
import java.nio.file.Path;

public class GppCppCompilation {

    public static ProcessOutput compileToProgramFromSource(Path program, Path sourceCode) {

        try {

            ProcessOutput compilatorOutput =
                    ProcessUtils.execute("g++", "-o", program.toString(), sourceCode.toString());

            return compilatorOutput;

        } catch (IOException | InterruptedException exception) {

            throw new RuntimeException(exception.getMessage());
        }
    }
}

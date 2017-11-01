package pl.edu.pg.examgeneratorng.util;

import org.apache.commons.io.IOUtils;
import pl.edu.pg.examgeneratorng.ProcessOutput;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class ProcessUtils {

    public static ProcessOutput execute(Path program) throws IOException, InterruptedException {

        return execute(program.toString());
    }

    public static ProcessOutput execute(String... arguments) throws IOException, InterruptedException {

        Process process = new ProcessBuilder(arguments).start();

        int status = process.waitFor();
        List<String> standardOutput = IOUtils.readLines(process.getInputStream());
        List<String> errorOutput = IOUtils.readLines(process.getErrorStream());

        return ProcessOutput.builder()
                .status(status)
                .errorOutput(errorOutput)
                .standardOutput(standardOutput).build();
    }
}

package pl.edu.pg.examgeneratorng;

import pl.edu.pg.examgeneratorng.util.ProcessUtils;

import java.io.*;
import java.nio.file.*;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static pl.edu.pg.examgeneratorng.GppCppCompilation.compileToProgramFromSource;
import static pl.edu.pg.examgeneratorng.util.FileUtils.directoryWithTemporaryFiles;
import static pl.edu.pg.examgeneratorng.util.StringUtils.splitByNewline;

public class GppCppProgramEvaluator implements CppProgramEvaluator {

    public ProgramOutput evaluate(String sourceCode){

        return evaluate(splitByNewline(sourceCode));
    }

    @Override
    public ProgramOutput evaluate(Iterable<String> sourceCode) {

        try {

            return evaluate(createTemporarySourceCodeFileFrom(sourceCode));
        } catch (IOException ioException) {

            throw new UncheckedIOException(ioException);
        }
    }

    private static Path createTemporarySourceCodeFileFrom(Iterable<String> sourceCode) throws IOException {

        Path temporarySourceCode = Files.createTempFile ("temporarySourceCode", ".cpp");

        return Files.write(temporarySourceCode, sourceCode, TRUNCATE_EXISTING);
    }

    private static ProgramOutput evaluate(Path sourceCode) {

        try {
            Path program = Paths.get
                    (directoryWithTemporaryFiles().toString() + File.separator + "program.exe");
            compileToProgramFromSource(program, sourceCode);

            ProcessOutput programOutput = ProcessUtils.execute(program);

            return new ProgramOutput(programOutput);

        } catch (IOException | InterruptedException exception) {

            throw new RuntimeException(exception);
        }
    }
}

package pl.edu.pg.examgeneratorng;

import pl.edu.pg.examgeneratorng.util.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static pl.edu.pg.examgeneratorng.util.FileUtils.directoryWithTemporaryFiles;
import static pl.edu.pg.examgeneratorng.util.SystemUtils.rootDirectory;
import static pl.edu.pg.examgeneratorng.util.SystemUtils.windowsGccCompilerDirectory;

public class GccCompiler implements Compiler {

    @Override
    public CompilerOutput compile(List<String> sourceCode) {

        try {

            return compile(createSourceCodeFileFrom(sourceCode));
        } catch (IOException | InterruptedException exception) {

            throw new RuntimeException(exception);
        }
    }

    private Path createSourceCodeFileFrom(Iterable<String> sourceCode) {

        try {
            Path sourceCodeFile = Files.createTempFile("sourceCode", ".cpp");

            return Files.write(sourceCodeFile, sourceCode);
        } catch (IOException ioException) {

            throw new UncheckedIOException(ioException);
        }
    }

    private CompilerOutput compile(Path sourceCode) throws IOException, InterruptedException {

        Path programPath = Paths.get
                (directoryWithTemporaryFiles() + File.separator + UUID.randomUUID().toString() + ".exe");

        Path workingDirectory = IS_OS_LINUX ? rootDirectory() : windowsGccCompilerDirectory();
        String commandToRunGcc = IS_OS_LINUX ? "g++" : workingDirectory + File.separator + "g++";
        ProcessOutput compilationProcessOutput = ProcessUtils.execute
                (workingDirectory, commandToRunGcc, "-o", programPath.toString(), sourceCode.toString());

        Program program = compilationProcessOutput.getStatus() == 0 ? new CppProgram(programPath) : null;

        return new CompilerOutput(compilationProcessOutput.getDiagnostics(), program);
    }
}

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

import static pl.edu.pg.examgeneratorng.util.FileUtils.directoryWithTemporaryFiles;

public class GccCompiler implements Compiler {

    @Override
    public CompilerOutput compile(List<String> sourceCode) {

        try{

            return compile(createSourceCodeFileFrom(sourceCode));
        }
        catch(IOException | InterruptedException exception){

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

        Path program = Paths.get(
                directoryWithTemporaryFiles() + File.separator + UUID.randomUUID().toString() + ".cpp");
        ProcessOutput compilationProcessOutput=
                ProcessUtils.execute("g++", "-o", program.toString(), sourceCode.toString());

        return new CompilerOutput(compilationProcessOutput.getDiagnostics(), new CppProgram(program));
    }
}

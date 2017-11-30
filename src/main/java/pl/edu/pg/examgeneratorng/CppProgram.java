package pl.edu.pg.examgeneratorng;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import pl.edu.pg.examgeneratorng.util.ProcessUtils;
import pl.edu.pg.examgeneratorng.util.SystemUtils;

import java.io.IOException;
import java.nio.file.Path;

import static pl.edu.pg.examgeneratorng.util.SystemUtils.isLinux;
import static pl.edu.pg.examgeneratorng.util.SystemUtils.windowsGccCompilerDirectory;

@AllArgsConstructor
public class CppProgram implements Program{
    @NonNull
    private Path program;

    @Override
    public ProcessOutput execute(){

        try{

            if(isLinux())
                return ProcessUtils.execute(program.toAbsolutePath());

            return ProcessUtils.execute(windowsGccCompilerDirectory(),program.toAbsolutePath().toString());
        }
        catch(IOException | InterruptedException exception){

            throw new RuntimeException(exception);
        }
    }
}

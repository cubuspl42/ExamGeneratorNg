package pl.edu.pg.examgeneratorng;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import pl.edu.pg.examgeneratorng.util.ProcessUtils;

import java.io.IOException;
import java.nio.file.Path;

@AllArgsConstructor
public class CppProgram implements Program{
    @NonNull
    private Path program;

    @Override
    public ProcessOutput execute(){

        try{

            return ProcessUtils.execute(program);
        }
        catch(IOException | InterruptedException exception){

            throw new RuntimeException(exception);
        }
    }
}

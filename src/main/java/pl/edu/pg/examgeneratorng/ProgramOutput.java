package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class ProgramOutput {
    @NonNull
    private List<String> standardOutput;
    @NonNull
    private List<String> errorOutput;

    public ProgramOutput(ProcessOutput processOutput){

        standardOutput = processOutput.getStandardOutput();
        errorOutput = processOutput.getErrorOutput();
    }
}

package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
class ProgramOutput {
    @NonNull
    private List<String> standardOutput;
    @NonNull
    private List<String> errorOutput;

    ProgramOutput(ProcessOutput processOutput){

        standardOutput = processOutput.getStandardOutput();
        errorOutput = processOutput.getErrorOutput();
    }
}

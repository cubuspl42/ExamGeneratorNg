package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
class ProgramOutput {
    @NonNull
    private List<String> lines;

    public ProgramOutput(ProcessOutput processOutput){

        this.lines = processOutput.getStandardOutput();
    }
}

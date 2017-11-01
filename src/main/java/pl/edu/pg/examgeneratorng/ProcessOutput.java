package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProcessOutput {
    @NonNull
    private List<String> standardOutput;
    @NonNull
    private List<String> errorOutput;
    private int status;
}

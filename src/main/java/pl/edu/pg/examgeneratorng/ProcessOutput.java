package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import pl.edu.pg.examgeneratorng.util.ListUtils;

import java.util.List;

@Value
@Builder
public class ProcessOutput {
    @NonNull
    List<String> standardOutput;
    @NonNull
    List<String> errorOutput;
    int status;

    List<String> getDiagnostics(){

        return ListUtils.concat(standardOutput, errorOutput);
    }
}

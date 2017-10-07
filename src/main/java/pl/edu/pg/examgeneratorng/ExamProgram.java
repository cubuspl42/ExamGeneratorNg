package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
class ExamProgram {
    @NonNull
    private String source;
    @NonNull
    private List<String> output;
}

package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class ExamProgram {
    @NonNull
    private LineString source;
    @NonNull
    private LineString output;

    public static ExamProgram emptyExamProgram() {

        return ExamProgram
                .builder()
                .source(LineString.fromSingleLine(""))
                .output(LineString.fromSingleLine(""))
                .build();
    }
}

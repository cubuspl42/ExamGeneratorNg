package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class ExamProgram {
    @NonNull
    private String source;
    @NonNull
    private ProgramOutput output;
}

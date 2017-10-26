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
}

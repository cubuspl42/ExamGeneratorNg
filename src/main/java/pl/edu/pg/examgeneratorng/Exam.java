package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
class Exam {
    @NonNull
    private String group;
    @NonNull
    private List<ExamProgram> programs;
}

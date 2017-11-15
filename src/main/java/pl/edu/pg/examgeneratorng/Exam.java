package pl.edu.pg.examgeneratorng;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
class Exam {
    @NonNull
    private String group;
    @NonNull
    private Map<ProgramId, ExamProgram> examProgramMap;
}

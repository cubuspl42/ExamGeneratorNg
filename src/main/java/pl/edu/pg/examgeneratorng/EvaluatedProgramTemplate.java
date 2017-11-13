package pl.edu.pg.examgeneratorng;

import lombok.Value;

@Value
class EvaluatedProgramTemplate {
    private ProgramTemplate programTemplate;
    private ProgramOutput programOutput;
}

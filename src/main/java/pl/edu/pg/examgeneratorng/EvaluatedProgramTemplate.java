package pl.edu.pg.examgeneratorng;

import lombok.Value;

@Value
public class EvaluatedProgramTemplate {
    private ProgramTemplate programTemplate;
    private ProgramOutput programOutput;
}

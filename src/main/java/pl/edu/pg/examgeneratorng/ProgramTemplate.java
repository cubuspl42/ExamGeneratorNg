package pl.edu.pg.examgeneratorng;

import lombok.Value;

import java.util.List;

@Value
public class ProgramTemplate {
    private List<LineTemplate> lineTemplates;
}

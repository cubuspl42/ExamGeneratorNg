package pl.edu.pg.examgeneratorng;

import lombok.Value;

import java.util.List;

@Value
class ProgramTemplate {
    private List<LineTemplate> lineTemplates;
}

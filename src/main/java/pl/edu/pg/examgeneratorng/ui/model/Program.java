package pl.edu.pg.examgeneratorng.ui.model;

import lombok.Value;
import pl.edu.pg.examgeneratorng.Group;
import pl.edu.pg.examgeneratorng.ProgramId;

import java.util.Map;

@Value
public class Program {
    private ProgramId programId;
    private Map<Group, GroupProgram> groupPrograms;
}

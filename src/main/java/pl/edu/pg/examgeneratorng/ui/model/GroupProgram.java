package pl.edu.pg.examgeneratorng.ui.model;

import javafx.beans.value.ObservableValue;
import lombok.Value;
import pl.edu.pg.examgeneratorng.ProgramId;
import pl.edu.pg.examgeneratorng.ProgramTemplate;

@Value
public class GroupProgram {
    private ProgramId programId;
    private ProgramTemplate programTemplate;
    private String sourceCode;
    private ObservableValue<String> stdout;
    private ObservableValue<String> stderr;
}

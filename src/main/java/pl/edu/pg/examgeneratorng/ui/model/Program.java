package pl.edu.pg.examgeneratorng.ui.model;

import javafx.beans.value.ObservableValue;
import lombok.Value;
import pl.edu.pg.examgeneratorng.ProgramId;
import pl.edu.pg.examgeneratorng.ProgramTemplate;

@Value
public class Program {
    private ProgramId programId;
    private ProgramTemplate programTemplate;
    private ObservableValue<String> data;
}

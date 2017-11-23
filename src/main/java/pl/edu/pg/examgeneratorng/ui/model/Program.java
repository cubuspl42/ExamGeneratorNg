package pl.edu.pg.examgeneratorng.ui.model;

import javafx.beans.value.ObservableValue;
import lombok.Value;
import pl.edu.pg.examgeneratorng.ProgramId;

@Value
public class Program {
    private ProgramId programId;
    private ObservableValue<String> data;
}

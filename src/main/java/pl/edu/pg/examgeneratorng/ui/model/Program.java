package pl.edu.pg.examgeneratorng.ui.model;

import javafx.beans.value.ObservableValue;
import lombok.Value;

@Value
public class Program {
    private ObservableValue<String> data;
}

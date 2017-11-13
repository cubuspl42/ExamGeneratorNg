package pl.edu.pg.examgeneratorng.ui.util;

import javafx.scene.Parent;
import lombok.Value;

@Value
public class ControlledNode<TController> {
    private Parent node;
    private TController controller;
}

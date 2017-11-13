package pl.edu.pg.examgeneratorng.ui.util;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;

public final class BindingUtils {
    public static void bindButtonEnabled(Button button, ObservableValue<Boolean> enabled) {
        button.setDisable(!enabled.getValue());
        button.getProperties().put(new Object(), enabled);
        enabled.addListener((observable, oldValue, newValue) -> {
            button.setDisable(!newValue);
        });
    }

    public static void bindChildren(
            Node targetNode, ObservableList<Node> targetChildren, ObservableValue<Node> sourceChild) {

        targetNode.getProperties().put(new Object(), sourceChild);

        if (sourceChild.getValue() != null) {
            targetChildren.setAll(sourceChild.getValue());
        }

        sourceChild.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                targetChildren.setAll(newValue);
            } else {
                targetChildren.setAll();
            }
        });
    }
}

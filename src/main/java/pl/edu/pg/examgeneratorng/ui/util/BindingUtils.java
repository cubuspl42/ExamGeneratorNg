package pl.edu.pg.examgeneratorng.ui.util;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import org.fxmisc.easybind.EasyBind;

public final class BindingUtils {
    public static void bindButtonEnabled(Button button, ObservableValue<Boolean> enabled) {
        button.setDisable(!enabled.getValue());
        button.getProperties().put(new Object(), enabled);
        enabled.addListener((observable, oldValue, newValue) -> {
            button.setDisable(!newValue);
        });
    }
}

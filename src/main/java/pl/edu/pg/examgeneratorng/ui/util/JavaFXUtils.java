package pl.edu.pg.examgeneratorng.ui.util;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import pl.edu.pg.examgeneratorng.ui.Main;

import java.io.IOException;

import static pl.edu.pg.examgeneratorng.ui.util.BindingUtils.bindChildren;

public final class JavaFXUtils {
    public static <TController> ControlledNode<TController> loadFxml(
            Class<? extends Main> aClass, String name
    ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(aClass.getResource(name));
        Parent root = fxmlLoader.load();
        TController controller = fxmlLoader.getController();
        return new ControlledNode<>(root, controller);
    }

    public static Group group(ObservableValue<Node> singleChild) {
        Group group = new Group();
        bindChildren(group, group.getChildren(), singleChild);
        return group;
    }
}

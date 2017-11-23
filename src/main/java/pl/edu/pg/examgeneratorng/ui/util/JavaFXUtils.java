package pl.edu.pg.examgeneratorng.ui.util;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.easybind.EasyBind;
import pl.edu.pg.examgeneratorng.ui.Main;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.fxmisc.easybind.EasyBind.listBind;
import static pl.edu.pg.examgeneratorng.ui.util.BindingUtils.bindChildren;

public final class JavaFXUtils {
    public static <TController> ControlledNode<TController> loadFxml(
            Class<?> aClass, String name
    ) {
        FXMLLoader fxmlLoader = new FXMLLoader(aClass.getResource(name));
        try {
            Parent root = fxmlLoader.load();
            TController controller = fxmlLoader.getController();
            return new ControlledNode<>(root, controller);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Group group(ObservableValue<Node> singleChild) {
        Group group = new Group();
        bindChildren(group, group.getChildren(), singleChild);
        return group;
    }

    public static TabPane tabPane(ObservableList<Tab> tabs) {
        TabPane tabPane = new TabPane();
        tabPane.getProperties().put(new Object(), tabs);
        listBind(tabPane.getTabs(), tabs);
        return tabPane;
    }
}

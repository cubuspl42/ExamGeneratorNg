package pl.edu.pg.examgeneratorng.ui.views;

import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.val;
import pl.edu.pg.examgeneratorng.ui.About;
import pl.edu.pg.examgeneratorng.ui.Main;
import pl.edu.pg.examgeneratorng.ui.controllers.AboutWindowController;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils;

public class AboutWindowView {

    public static Parent aboutWindowView(Application application, Stage stage) {
        val aboutWindow = JavaFXUtils.<AboutWindowController>loadFxml(About.class, "/AboutWindow.fxml");
        aboutWindow.getController().initialize(application, stage);
        return aboutWindow.getNode();
    }
}

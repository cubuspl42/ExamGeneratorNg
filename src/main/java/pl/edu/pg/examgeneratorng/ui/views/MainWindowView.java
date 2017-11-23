package pl.edu.pg.examgeneratorng.ui.views;

import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.val;
import pl.edu.pg.examgeneratorng.ui.Main;
import pl.edu.pg.examgeneratorng.ui.controllers.MainWindowController;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils;

public class MainWindowView {
    public static Parent mainWindowView(Application application, Stage stage) {
        val mainWindowCn = JavaFXUtils.<MainWindowController>loadFxml(Main.class, "/MainWindow.fxml");
        mainWindowCn.getController().initialize(application, stage);
        return mainWindowCn.getNode();
    }
}

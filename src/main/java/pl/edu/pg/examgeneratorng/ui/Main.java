package pl.edu.pg.examgeneratorng.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.val;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils;

import static pl.edu.pg.examgeneratorng.LoggingConfig.configureLogging;

public class Main extends javafx.application.Application {
    public static void main(String[] args) {
        configureLogging();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Exam Generator");

        val application = new Application();

        val mainWindow = JavaFXUtils.<MainWindowController>loadFxml(getClass(), "/MainWindow.fxml");
        mainWindow.getController().initialize(application, stage);

        stage.setScene(new Scene(mainWindow.getNode(), 640, 480));
        stage.show();
    }

    private static Parent mainWindowView(Application application, Stage stage) {
//        return new BorderPane(Views.group(map(mainWindowVm.getOpenedOproject(), workspaceVm -> {
//            if (workspaceVm != null) {
//                return Views.workspaceView(workspaceVm);
//            } else {
//                return Views.initialView(mainWindowVm, stage);
//            }
//        })));

        Button button = new Button("Foo");

        ProgressBar progressBar = new ProgressBar(0.5);
        progressBar.setMinHeight(button.getPrefHeight());
        progressBar.setPrefHeight(button.getPrefHeight());

        progressBar.setMaxWidth(Double.POSITIVE_INFINITY);

        val hBox = new HBox(button, progressBar);

        HBox.setHgrow(button, Priority.ALWAYS);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        val vBox = new VBox(
                hBox,
                new Text("foo")
        );

        return vBox;
    }
}

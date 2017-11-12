package pl.edu.pg.examgeneratorng.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static org.fxmisc.easybind.EasyBind.map;
import static pl.edu.pg.examgeneratorng.LoggingConfig.configureLogging;

public class Main extends Application {
    public static void main(String[] args) {
        configureLogging();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Exam Generator");
        primaryStage.setScene(new Scene(mainWindowView(new MainWindowVm(), primaryStage), 640, 480));
        primaryStage.show();
    }

    private static Parent mainWindowView(MainWindowVm mainWindowVm, Stage stage) {
        return new BorderPane(Views.group(map(mainWindowVm.getOpenedWorkspace(), workspaceVm -> {
            if (workspaceVm != null) {
                return Views.workspaceView(workspaceVm);
            } else {
                return Views.initialView(mainWindowVm, stage);
            }
        })));
    }
}

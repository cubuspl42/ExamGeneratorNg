package pl.edu.pg.examgeneratorng.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.views.MainWindowView;

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
        Parent view = MainWindowView.mainWindowView(application, stage);

        stage.setScene(new Scene(view, 640, 480));
        stage.show();
    }

}

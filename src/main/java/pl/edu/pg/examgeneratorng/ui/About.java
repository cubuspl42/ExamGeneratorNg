package pl.edu.pg.examgeneratorng.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.val;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.views.AboutWindowView;

public class About extends javafx.application.Application{

    public void startWithParent(Window parent) throws Exception {

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parent);

        start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("About Exam Generator");

        val application = new Application();
        stage.setResizable(false);
        Parent view = AboutWindowView.aboutWindowView(application, stage);

        stage.setScene(new Scene(view, 600, 400));
        stage.showAndWait();
    }
}

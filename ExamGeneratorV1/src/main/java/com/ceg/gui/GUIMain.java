package com.ceg.gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

public class GUIMain extends Application {

    /**
     * Otwiera główne okno programu.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        GUIMainController.show();
    }

    /**
     * Uruchamia program napisany w JavaFX, wywołuje pośrednio metodę start.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}

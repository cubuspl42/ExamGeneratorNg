package pl.edu.pg.examgeneratorng.ui.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import pl.edu.pg.examgeneratorng.ui.model.Application;

import javafx.scene.text.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllLines;
import static pl.edu.pg.examgeneratorng.util.StringUtils.joinLines;

public class AboutWindowController {
    @FXML
    private Text description;

    private Application application;

    private Stage stage;

    public void initialize(Application application, Stage stage) {
        this.application = application;
        this.stage = stage;

        String descriptionText =
                "\t\t\t\tExam Generator jest wspaniałym narzędziem które, w dużym stopniu\n" +
                "\n" +
                "\t\tułatwia proces tworzenia testów egzaminacyjnych z języka C i C++. Na podstawie\n" +
                "\n" +
                "\t\twskazanych kodów źródłowych umożliwia podzielenie egzaminu na grupy, wskazuję\n" +
                "\n" +
                "\t\tbłędy kompilacji (jeśli występują) i wiele, wiele innych.";
        description.setText(descriptionText);
    }
}

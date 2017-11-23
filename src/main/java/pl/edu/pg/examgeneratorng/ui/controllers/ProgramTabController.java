package pl.edu.pg.examgeneratorng.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import pl.edu.pg.examgeneratorng.ui.model.GroupProgram;

public class ProgramTabController {
    @FXML
    private Text programTemplateText;

    @FXML
    private Text standardOutputText;

    @FXML
    private Text standardErrorText;

    private GroupProgram groupProgram;

    public void initialize(GroupProgram groupProgram) {
        this.groupProgram = groupProgram;

        programTemplateText.setText(groupProgram.getSourceCode());

        standardOutputText.textProperty().bind(groupProgram.getStdout());

        standardErrorText.textProperty().bind(groupProgram.getStderr());
    }
}

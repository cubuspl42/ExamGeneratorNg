package pl.edu.pg.examgeneratorng.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.val;
import pl.edu.pg.examgeneratorng.Group;
import pl.edu.pg.examgeneratorng.ProgramVariant;
import pl.edu.pg.examgeneratorng.ui.model.GroupProgram;

import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;
import static pl.edu.pg.examgeneratorng.util.StringUtils.joinLines;

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

package pl.edu.pg.examgeneratorng.ui.views;

import javafx.scene.Parent;
import lombok.val;
import pl.edu.pg.examgeneratorng.ui.controllers.ProgramTabController;
import pl.edu.pg.examgeneratorng.ui.model.GroupProgram;
import pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils;

public class GroupProgramView {
    public static Parent groupProgramView(GroupProgram groupProgram) {
        val programTabCn = JavaFXUtils.<ProgramTabController>loadFxml(GroupProgramView.class, "/ProgramTab.fxml");
        programTabCn.getController().initialize(groupProgram);

        return programTabCn.getNode();
    }
}

package pl.edu.pg.examgeneratorng.ui.views;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.val;
import pl.edu.pg.examgeneratorng.Group;
import pl.edu.pg.examgeneratorng.ui.controllers.ProgramTabController;
import pl.edu.pg.examgeneratorng.ui.model.GroupProgram;
import pl.edu.pg.examgeneratorng.ui.model.Program;
import pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils;

import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;
import static org.fxmisc.easybind.EasyBind.map;
import static pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils.tabPane;
import static pl.edu.pg.examgeneratorng.ui.views.GroupProgramView.groupProgramView;

public class ProgramView {
    public static Parent programView(Program program) {
        return tabPane(
                observableArrayList(program.getGroupPrograms().entrySet().stream().map(entry -> {
                    val group = entry.getKey();
                    val groupProgram = entry.getValue();
                    return new Tab(group.toString(), groupProgramView(groupProgram));
                }).collect(Collectors.toList()))
        );
    }
}

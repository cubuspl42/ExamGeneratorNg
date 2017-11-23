package pl.edu.pg.examgeneratorng.ui.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.val;
import org.fxmisc.easybind.EasyBind;
import pl.edu.pg.examgeneratorng.Diagnostic;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.model.GroupProgram;
import pl.edu.pg.examgeneratorng.ui.model.Project;
import pl.edu.pg.examgeneratorng.ui.model.ProjectTask;

import java.io.File;

import static org.fxmisc.easybind.EasyBind.map;
import static org.fxmisc.easybind.EasyBind.monadic;
import static org.fxmisc.easybind.EasyBind.select;
import static pl.edu.pg.examgeneratorng.ui.util.BindingUtils.bindButtonEnabled;
import static pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils.tabPane;
import static pl.edu.pg.examgeneratorng.ui.views.GroupProgramView.groupProgramView;
import static pl.edu.pg.examgeneratorng.ui.views.ProgramView.programView;

public class MainWindowController {
    @FXML
    private Button generateButton;

    @FXML
    private Text statusText;

    @FXML
    private GridPane progressGridPane;

    @FXML
    private VBox programsVbox;

    @FXML
    private BorderPane contentBorderPane;

    private Application application;

    private Stage stage;

    public void initialize(Application application, Stage stage) {
        this.application = application;
        this.stage = stage;

        stage.titleProperty().bind(
                Bindings.concat("Exam Generator", monadic(application.getOpenProject())
                        .map(project -> " - [" + project.getWorkspacePath().toString() + "]")
                        .orElse(""))
        );

        statusText.textProperty().bind(
                select(application.getOpenProject())
                        .select(Project::getProjectTask)
                        .selectObject(ProjectTask::getState)
                        .map(Enum::toString)
                        .orElse("IDLE")
        );

        bindButtonEnabled(generateButton,
                select(application.getOpenProject())
                        .selectObject(Project::canStartTask)
                        .orElse(false)
        );

        val progressBarConstraint = progressGridPane.getColumnConstraints().get(0);

        progressBarConstraint.percentWidthProperty().bind(
                select(application.getOpenProject())
                        .select(Project::getProjectTask)
                        .selectObject(ProjectTask::getProgress)
                        .map(x -> 100.0 * (Double) x)
                        .orElse(0.0)
        );

        val programs = select(application.getOpenProject())
                .select(Project::getProjectTask)
                .selectObject(ProjectTask::getPrograms)
                .map(FXCollections::observableArrayList);

        contentBorderPane.topProperty().bind(programs.map(programs1 -> tabPane(
                map(programs1, program -> new Tab(
                        program.getProgramId().toString(),
                        programView(program)
                ))
        )));

        contentBorderPane.centerProperty().bind(contentView());
    }

    private ObservableValue<Node> contentView() {
        return select(application.getOpenProject())
                .selectObject(Project::getProjectTask)
                .map(projectTask -> diagnosticsView(projectTask.getDiagnostics()));
    }

    private Node diagnosticsView(ObservableList<Diagnostic> diagnostics) {
        ListView<Diagnostic> listView = new ListView<>(diagnostics);
        BorderPane.setAlignment(listView, Pos.CENTER);

        listView.setCellFactory(param -> new ListCell<Diagnostic>() {
                    @Override
                    public void updateItem(Diagnostic item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Text text = new Text(item.getMessage());
                            text.wrappingWidthProperty().bind(listView.widthProperty());
                            setGraphic(text);
                        }
                    }
                }
        );

        return listView;
    }

    @FXML
    private void onGeneratePressed(ActionEvent actionEvent) {
        application.getOpenProject().get().startTask();
    }

    @FXML
    private void onFileOpen(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open project");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            application.openProject(file.toPath());
        }
    }
}

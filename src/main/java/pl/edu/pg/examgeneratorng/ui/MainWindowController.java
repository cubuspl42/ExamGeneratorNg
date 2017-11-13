package pl.edu.pg.examgeneratorng.ui;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.val;
import pl.edu.pg.examgeneratorng.Diagnostic;
import pl.edu.pg.examgeneratorng.ui.model.Application;
import pl.edu.pg.examgeneratorng.ui.model.Project;
import pl.edu.pg.examgeneratorng.ui.model.ProjectTask;
import pl.edu.pg.examgeneratorng.ui.model.ProjectTask.State;

import java.io.File;

import static org.fxmisc.easybind.EasyBind.select;
import static pl.edu.pg.examgeneratorng.ui.util.BindingUtils.bindButtonEnabled;
import static pl.edu.pg.examgeneratorng.ui.util.JavaFXUtils.group;

public class MainWindowController {
    @FXML
    private Button generateButton;

    @FXML
    private Text statusText;

    @FXML
    private GridPane progressGridPane;

    @FXML
    private BorderPane contentBorderPane;

    private Application application;

    private Stage stage;

    void initialize(Application application, Stage stage) {
        this.application = application;
        this.stage = stage;

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
        application.openProject(file.toPath());
    }
}

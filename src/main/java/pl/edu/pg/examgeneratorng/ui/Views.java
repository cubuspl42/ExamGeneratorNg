package pl.edu.pg.examgeneratorng.ui;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import static javafx.beans.binding.Bindings.not;
import static javafx.beans.binding.Bindings.selectBoolean;
import static org.fxmisc.easybind.EasyBind.select;
import static pl.edu.pg.examgeneratorng.ui.util.BindingUtils.bindButtonEnabled;

final class Views {
    static Node workspaceView(WorkspaceVm workspaceVm) {
        Button button = new Button("Generate exams");
        bindButtonEnabled(button, not(selectBoolean(workspaceVm.isBusy())));
        button.setOnAction(event -> workspaceVm.generateExams());

        Node statusNode = group(
                select(workspaceVm.getTask())
                        .selectObject(Task::stateProperty)
                        .map(state -> (Node) new Text("Status: " + state))
        );

        Node errorNode = group(
                select(workspaceVm.getTask())
                        .selectObject(Task::exceptionProperty)
                        .map(e -> {
                            Text text = new Text(e.getMessage());
                            text.setFill(Color.RED);
                            return text;
                        })
        );

        return new VBox(button, statusNode, errorNode);
    }

    static Node initialView(MainWindowVm mainWindowVm, Stage stage) {
        Button button = new Button("Open workspace...");
        bindButtonEnabled(button, mainWindowVm.canOpenWorkspace());

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open workspace");

        button.setOnAction(e -> {
            File file = directoryChooser.showDialog(stage);
            mainWindowVm.openWorkspace(file.toPath());
        });

        return button;
    }

    static Group group(ObservableValue<Node> singleChild) {
        Group group = new Group();
        bindChildren(group, group.getChildren(), singleChild);
        return group;
    }

    private static void bindChildren(
            Node targetNode, ObservableList<Node> targetChildren, ObservableValue<Node> sourceChild) {

        targetNode.getProperties().put(new Object(), sourceChild);

        if (sourceChild.getValue() != null) {
            targetChildren.setAll(sourceChild.getValue());
        }

        sourceChild.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                targetChildren.setAll(newValue);
            } else {
                targetChildren.setAll();
            }
        });
    }
}

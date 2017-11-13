package pl.edu.pg.examgeneratorng.ui.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

import java.nio.file.Path;

public class Application {
    private final SimpleObjectProperty<Project> openProjectProperty = new SimpleObjectProperty<>();

    public ObservableObjectValue<Project> getOpenProject() {
        return openProjectProperty;
    }

    public void openProject(Path workspacePath) {
//        Preconditions.checkState(canOpenProject().getValue());
        openProjectProperty.set(new Project(workspacePath));
    }
}

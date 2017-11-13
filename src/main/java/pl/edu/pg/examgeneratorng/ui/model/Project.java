package pl.edu.pg.examgeneratorng.ui.model;

import com.google.common.base.Preconditions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import lombok.Getter;

import java.nio.file.Path;

import static org.fxmisc.easybind.EasyBind.select;
import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;

public class Project {
    @Getter
    private final Path workspacePath;

    private final SimpleObjectProperty<ProjectTask> taskProperty = new SimpleObjectProperty<>();

    Project(Path workspacePath) {
        this.workspacePath = workspacePath;
    }

    public ObservableValue<ProjectTask> getProjectTask() {
        return taskProperty;
    }

    public ObservableValue<Boolean> canStartTask() {
        return select(taskProperty)
                .selectObject(ProjectTask::getState)
                .map(state -> state != ProjectTask.State.RUNNING)
                .orElse(true);
    }

    public void startTask() {
        Preconditions.checkState(canStartTask().getValue());
        taskProperty.set(createProjectTask());
    }

    private ProjectTask createProjectTask() {
        return new ProjectTask(workspacePath);
    }
}

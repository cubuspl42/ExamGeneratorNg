package pl.edu.pg.examgeneratorng.ui;

import com.google.common.base.Preconditions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import lombok.Getter;

import java.nio.file.Path;

import static org.fxmisc.easybind.EasyBind.select;
import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;

class WorkspaceVm {
    @Getter
    private final Path workspacePath;

    private final SimpleObjectProperty<Task<Boolean>> task = new SimpleObjectProperty<>();

    ObservableValue<Boolean> isBusy() {
        return select(task)
                .selectObject(Task::runningProperty)
                .orElse(false);
    }

    ObservableValue<Task<Boolean>> getTask() {
        return task;
    }

    WorkspaceVm(Path workspacePath) {
        this.workspacePath = workspacePath;
    }

    void generateExams() {
        Preconditions.checkState(!isBusy().getValue());
        task.set(startExamGenerationTask());
    }

    private Task<Boolean> startExamGenerationTask() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                generateAllExamVariants(workspacePath);
                return true;
            }
        };

        new Thread(task).start();

        return task;
    }

}

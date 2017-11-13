package pl.edu.pg.examgeneratorng.ui.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import lombok.Getter;
import pl.edu.pg.examgeneratorng.Diagnostic;
import pl.edu.pg.examgeneratorng.DiagnosticKind;

import java.nio.file.Path;

import static javafx.application.Platform.runLater;
import static org.fxmisc.easybind.EasyBind.select;
import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;

public class ProjectTask {
    private final Path workspacePath;

    public enum State {
        RUNNING,
        FAILED,
        SUCCEEDED
    }

    private final SimpleObjectProperty<Task<Void>> taskProperty = new SimpleObjectProperty<>();

    @Getter
    private final ObservableList<Diagnostic> diagnostics = FXCollections.observableArrayList();

    ProjectTask(Path workspacePath) {
        this.workspacePath = workspacePath;
        taskProperty.set(createTask());
    }

    public ObservableDoubleValue getProgress() {
        return taskProperty.get().progressProperty();
    }

    public ObservableValue<State> getState() {
        return select(taskProperty)
                .selectObject(Task::stateProperty)
                .map(ProjectTask::mapState);
    }

    private Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 1);

                try {
                    generateAllExamVariants(workspacePath);
                } catch (Exception e) {
                    pushDiagnostic(new Diagnostic(DiagnosticKind.ERROR, e.getMessage()));
                    throw e;
                }

                pushDiagnostic(new Diagnostic(DiagnosticKind.INFO, "Exam generated successfully"));

                updateProgress(1, 1);

                return null;
            }
        };
        new Thread(task).start();
        return task;
    }

    private void pushDiagnostic(Diagnostic diagnostic) {
        runLater(() -> diagnostics.add(diagnostic));
    }

    private static State mapState(Worker.State state) {
        switch (state) {
            case READY:
                return State.RUNNING;
            case SCHEDULED:
                return State.RUNNING;
            case RUNNING:
                return State.RUNNING;
            case SUCCEEDED:
                return State.SUCCEEDED;
            case FAILED:
                return State.FAILED;
            default:
                throw new AssertionError();
        }
    }
}

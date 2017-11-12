package pl.edu.pg.examgeneratorng.ui;

import com.google.common.base.Preconditions;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import lombok.Getter;

import java.nio.file.Path;

import static org.fxmisc.easybind.EasyBind.select;

class MainWindowVm {
    private final SimpleObjectProperty<WorkspaceVm> openedWorkspace = new SimpleObjectProperty<>();

    ObservableObjectValue<WorkspaceVm> getOpenedWorkspace() {
        return openedWorkspace;
    }

    ObservableValue<Boolean> canOpenWorkspace() {
        return select(openedWorkspace)
                .selectObject(WorkspaceVm::isBusy)
                .orElse(true);
    }

    void openWorkspace(Path workspacePath) {
        Preconditions.checkState(canOpenWorkspace().getValue());
        openedWorkspace.set(new WorkspaceVm(workspacePath));
    }
}

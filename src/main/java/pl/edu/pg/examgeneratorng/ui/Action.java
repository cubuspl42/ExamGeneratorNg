package pl.edu.pg.examgeneratorng.ui;

import javafx.beans.value.ObservableValue;

public class Action {
    private final ObservableValue<Boolean> enabled;
    private final Runnable runnable;

    Action(ObservableValue<Boolean> enabled, Runnable runnable) {
        this.enabled = enabled;
        this.runnable = runnable;
    }

    public ObservableValue<Boolean> getEnabled() {
        return enabled;
    }

    public void run() {
        runnable.run();
    }
}

package pl.edu.pg.examgeneratorng.ui.util;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import org.lucidfox.jpromises.Promise;
import org.lucidfox.jpromises.PromiseFactory;
import org.lucidfox.jpromises.core.ThrowingSupplier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static javafx.beans.binding.Bindings.divide;

public class PromiseContext {
    private final PromiseFactory promiseFactory;

    private final ExecutorService executorService = new ForkJoinPool();

    private final SimpleDoubleProperty workDone = new SimpleDoubleProperty(0.0);

    private final SimpleDoubleProperty workTotal = new SimpleDoubleProperty(0.0);

    public PromiseContext(PromiseFactory promiseFactory) {
        this.promiseFactory = promiseFactory;
    }

    public <T> Promise<T> supplyAsync(ThrowingSupplier<? extends T> supplier) {
        workTotal.set(workTotal.get() + 1.0);
        Promise<T> promise = promiseFactory.supplyAsync(supplier, executorService::submit);
        promise.thenRun(() -> workDone.set(workDone.get() + 1.0));
        return promise;
    }

    public ObservableNumberValue getProgress() {
        return divide(workDone, workTotal);
    }
}

package pl.edu.pg.examgeneratorng.ui.util;

import com.google.common.base.Preconditions;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
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

    private final SimpleObjectProperty<Throwable> exception = new SimpleObjectProperty<>();

    public PromiseContext(PromiseFactory promiseFactory) {
        this.promiseFactory = promiseFactory;
    }

    public <T> Promise<T> supplyAsync(ThrowingSupplier<? extends T> supplier) {
        Preconditions.checkState(exception.get() == null);

        workTotal.set(workTotal.get() + 1.0);
        Promise<T> promise = promiseFactory.supplyAsync(supplier, executorService::submit);

        promise.thenRun(() -> workDone.set(workDone.get() + 1.0));

        promise.onExceptionAccept(exception::set);

        return promise;
    }

    public ObservableNumberValue getProgress() {
        return divide(workDone, workTotal);
    }

    public ObservableObjectValue<Throwable> getException() {
        return exception;
    }
}

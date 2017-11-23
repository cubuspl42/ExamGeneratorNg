package pl.edu.pg.examgeneratorng.ui.util;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import lombok.Value;
import lombok.val;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;
import org.fxmisc.easybind.monadic.MonadicObservableValue;
import org.lucidfox.jpromises.Promise;
import org.lucidfox.jpromises.PromiseFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.fxmisc.easybind.EasyBind.monadic;

public class PromiseUtils {
    @Value
    private static class MapEntry<K, V> {
        private K key;
        private V value;
    }

    public static <K, V> Promise<Map<K, V>> all(PromiseFactory factory, Map<K, Promise<V>> inputMap) {
        List<Promise<PromiseUtils.MapEntry<K, V>>> entryPromises = inputMap.entrySet().stream()
                .map(entry -> {
                    val key = entry.getKey();
                    val valuePromise = entry.getValue();
                    return valuePromise.then(value ->
                            factory.resolve(new MapEntry<>(key, value))
                    );
                })
                .collect(Collectors.toList());


        return factory
                .all(entryPromises)
                .then(entries -> {
                    Map<K, V> outputMap = entries.stream()
                            .collect(Collectors.toMap(MapEntry::getKey, MapEntry::getValue));
                    return factory.resolve(outputMap);
                });
    }

    public static <T> ObservableValue<T> promiseToObservableValue(Promise<T> promise) {
        SimpleObjectProperty<T> property = new SimpleObjectProperty<>();
        promise.thenAccept(property::set);
        return property;
    }

    public static <T> MonadicObservableValue<T> promiseToMonadic(Promise<T> promise) {
        return monadic(promiseToObservableValue(promise));
    }
}

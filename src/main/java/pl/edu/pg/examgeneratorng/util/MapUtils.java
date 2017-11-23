package pl.edu.pg.examgeneratorng.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.unmodifiableMap;

public final class MapUtils {
    public static <K, V1, V2> Map<K, V2> mapMapByValue(Map<K, V1> inputMap, Function<V1, V2> valueMapper) {
        return mapMapByEntry(inputMap, entry -> valueMapper.apply(entry.getValue()));
    }

    public static <K, V1, V2> Map<K, V2> mapMapByEntry(
            Map<K, V1> inputMap, Function<Map.Entry<K, V1>, V2> valueMapper
    ) {
        Map<K, V2> outputMap = new HashMap<>();
        for (Map.Entry<K, V1> entry : inputMap.entrySet()) {
            V2 value = valueMapper.apply(entry);
            outputMap.put(entry.getKey(), value);
        }
        return unmodifiableMap(outputMap);
    }

    public static <K, V> Map<K, V> mapToMap(Collection<K> keys, Function<K, V> mapper) {
        Map<K, V> outputMap = new HashMap<>();
        for (K key : keys) {
            V value = mapper.apply(key);
            outputMap.put(key, value);
        }
        return outputMap;
    }
}

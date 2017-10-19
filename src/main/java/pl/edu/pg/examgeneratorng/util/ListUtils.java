package pl.edu.pg.examgeneratorng.util;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtils {
    public static <T> List<T> concat(List<T> list0, List<T> list1) {
        return Stream.concat(list0.stream(), list1.stream())
                .collect(Collectors.toList());
    }

    public static <T> List<T> emptyMutableList() {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) new ArrayList();
        return result;
    }

    public static <T, R> List<R> mapPairs(List<T> xs, BiFunction<T, T, R> mapper) {
        Preconditions.checkArgument(xs.size() > 1);

        List<R> result = new ArrayList<>(xs.size() - 1);

        for (int i = 0; i < xs.size() - 1; ++i) {
            T a = xs.get(i);
            T b = xs.get(i + 1);
            R r = mapper.apply(a, b);
            result.add(r);
        }

        return result;
    }
}

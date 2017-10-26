package pl.edu.pg.examgeneratorng.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtils {
    public static <T> List<T> concat(List<T> list0, List<T> list1) {
        return Stream.concat(list0.stream(), list1.stream())
                .collect(Collectors.toList());
    }

    public static <T> List<T> concat(List<T> list0, List<T> list1, List<T> list2) {
        return concat(list0, concat(list1, list2));
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

    public static <T> List<T> sandwich(T head, List<T> xs, T tail) {
        return concat(ImmutableList.of(head), xs, ImmutableList.of(tail));
    }

    public static <T, R> List<R> mapPath(
            List<T> xs,
            Function<T, R> vertexMapper,
            BiFunction<T, T, R> edgeMapper,
            T beginGhostVertex,
            T endGhostVertex
    ) {
        List<T> xs1 = sandwich(beginGhostVertex, xs, endGhostVertex);
        List<R> result = new ArrayList<>(xs.size() + 2);
        for (int i = 0; i < xs1.size() - 1; ++i) {
            if (i > 0) {
                R e0 = vertexMapper.apply(xs1.get(i));
                result.add(e0);
            }
            T v0 = xs1.get(i);
            T v1 = xs1.get(i + 1);
            R e1 = edgeMapper.apply(v0, v1);
            result.add(e1);
        }
        return result;
    }

    public static <T> List<T> join(List<List<T>> xs, Supplier<List<T>> sepSupplier) {
        List<T> result = new ArrayList<>(xs.size() * 2);
        for (int i = 0; i < xs.size(); ++i) {
            result.addAll(xs.get(i));
            if (i != xs.size() - 1) {
                List<T> sep = sepSupplier.get();
                result.addAll(sep);
            }
        }
        return result;
    }
}

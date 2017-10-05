package pl.edu.pg.examgeneratorng.util;

import java.util.ArrayList;
import java.util.List;
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
}

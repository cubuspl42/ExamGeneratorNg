package pl.edu.pg.examgeneratorng.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListUtilsTest {
    @Test
    public void concat_twoLists() throws Exception {
        List<Integer> actual = ListUtils.concat(ImmutableList.of(1, 2, 3), ImmutableList.of(4, 5, 6));
        List<Integer> expected = ImmutableList.of(1, 2, 3, 4, 5, 6);
        assertEquals(expected, actual);
    }

    @Test
    public void concat_threeLists() throws Exception {
        List<Integer> actual = ListUtils.concat(
                ImmutableList.of(1, 2, 3), ImmutableList.of(4, 5, 6), ImmutableList.of(7, 8));
        List<Integer> expected = ImmutableList.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mapPairs_empty() throws Exception {
        ListUtils.mapPairs(ImmutableList.of(), (a, b) -> null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mapPairs_oneElement() throws Exception {
        ListUtils.mapPairs(ImmutableList.of("a"), (a, b) -> null);
    }

    @Test
    public void mapPairs_twoElements() throws Exception {
        List<String> actual = ListUtils.mapPairs(ImmutableList.of("a", "b"), ListUtilsTest::add);
        List<String> expected = ImmutableList.of("ab");
        assertEquals(expected, actual);
    }

    @Test
    public void mapPairs_threeElements() throws Exception {
        List<String> actual = ListUtils.mapPairs(ImmutableList.of("a", "b", "c"), ListUtilsTest::add);
        List<String> expected = ImmutableList.of("ab", "bc");
        assertEquals(expected, actual);
    }

    @Test
    public void mapPairs_fourElements() throws Exception {
        List<String> actual = ListUtils.mapPairs(ImmutableList.of("a", "b", "c", "d"), ListUtilsTest::add);
        List<String> expected = ImmutableList.of("ab", "bc", "cd");
        assertEquals(expected, actual);
    }

    @Test
    public void sandwich_simple() {
        List<Integer> actual = ListUtils.sandwich(1, ImmutableList.of(2, 3, 4), 5);
        List<Integer> expected = ImmutableList.of(1, 2, 3, 4, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void mapPath_empty() {
        List<String> actual = ListUtils.mapPath(
                ImmutableList.of(),
                Object::toString,
                ListUtilsTest::concatObjects,
                1,
                2
        );
        List<String> expected = ImmutableList.of("12");
        assertEquals(expected, actual);
    }

    @Test
    public void mapPath_simple() {
        List<String> actual = ListUtils.mapPath(
                ImmutableList.of(2, 3, 4),
                Object::toString,
                ListUtilsTest::concatObjects,
                1,
                5
        );
        List<String> expected = ImmutableList.of("12", "2", "23", "3", "34", "4", "45");
        assertEquals(expected, actual);
    }

    private static String add(String a, String b) {
        return a + b;
    }

    private static String concatObjects(Object v1, Object v2) {
        return "" + v1 + v2;
    }
}
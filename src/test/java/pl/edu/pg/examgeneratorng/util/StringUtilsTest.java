package pl.edu.pg.examgeneratorng.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static pl.edu.pg.examgeneratorng.util.StringUtils.splitByNewline;
import static pl.edu.pg.examgeneratorng.util.StringUtils.stringToLines;

public class StringUtilsTest {
    @Test
    public void splitByNewline_empty() throws Exception {
        List<String> actual = splitByNewline("");
        List<String> expected = ImmutableList.of("");
        assertEquals(expected, actual);
    }

    @Test
    public void splitByNewline_singleLineDos() throws Exception {
        List<String> actual = splitByNewline("foo");
        List<String> expected = ImmutableList.of("foo");
        assertEquals(expected, actual);
    }

    @Test
    public void splitByNewline_singleLineUnix() throws Exception {
        List<String> actual = splitByNewline("foo\n");
        List<String> expected = ImmutableList.of("foo", "");
        assertEquals(expected, actual);
    }

    @Test
    public void splitByNewline_multipleLinesDos() throws Exception {
        List<String> actual = splitByNewline("foo\nbar");
        List<String> expected = ImmutableList.of("foo", "bar");
        assertEquals(expected, actual);
    }

    @Test
    public void splitByNewline_multipleLinesUnix() throws Exception {
        List<String> actual = splitByNewline("foo\nbar\n");
        List<String> expected = ImmutableList.of("foo", "bar", "");
        assertEquals(expected, actual);
    }

    @Test
    public void splitByNewline_singleNewline() throws Exception {
        List<String> actual = splitByNewline("\n");
        List<String> expected = ImmutableList.of("", "");
        assertEquals(expected, actual);
    }

    @Test
    public void splitByNewline_twoNewlines() throws Exception {
        List<String> actual = splitByNewline("\n\n");
        List<String> expected = ImmutableList.of("", "", "");
        assertEquals(expected, actual);
    }

    @Test
    public void stringToLines_empty() {
        String str = "";
        List<String> actual = stringToLines(str);
        List<String> expected = ImmutableList.of();
        assertEquals(expected, actual);
    }

    @Test
    public void stringToLines_singleLineDos() throws Exception {
        List<String> actual = stringToLines("foo");
        List<String> expected = ImmutableList.of("foo");
        assertEquals(expected, actual);
    }
    @Test
    public void stringToLines_singleLineUnix() throws Exception {
        List<String> actual = stringToLines("foo\n");
        List<String> expected = ImmutableList.of("foo");
        assertEquals(expected, actual);
    }

    @Test
    public void stringToLines_multipleLinesDos() {
        String str = "foo\nbar\nbaz";
        List<String> actual = stringToLines(str);
        List<String> expected = ImmutableList.of("foo", "bar", "baz");
        assertEquals(expected, actual);
    }

    @Test
    public void stringToLines_multipleLinesUnix() {
        String str = "foo\nbar\nbaz\n";
        List<String> actual = stringToLines(str);
        List<String> expected = ImmutableList.of("foo", "bar", "baz");
        assertEquals(expected, actual);
    }

    @Test
    public void stringToLines_singleNewline() {
        String str = "\n";
        List<String> actual = stringToLines(str);
        List<String> expected = ImmutableList.of("");
        assertEquals(expected, actual);
    }

    @Test
    public void stringToLines_twoNewlines() {
        String str = "\n\n";
        List<String> actual = stringToLines(str);
        List<String> expected = ImmutableList.of("", "");
        assertEquals(expected, actual);
    }
}
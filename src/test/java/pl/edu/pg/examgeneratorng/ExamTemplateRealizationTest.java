package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import pl.edu.pg.examgeneratorng.exceptions.ExamTemplateRealizationException;

import static org.junit.Assert.assertEquals;
import static pl.edu.pg.examgeneratorng.ExamTemplateRealization.dumpProgram;

public class ExamTemplateRealizationTest {
    private static final LineString PROGRAM_SOURCE = new LineString(ImmutableList.of("foo", "bar", "baz"));

    @Test
    public void dumpProgram_fits() {
        Placeholder placeholder = new Placeholder(PlaceholderKind.CODE, 1, 0, 4, 5);
        LineString actual = dumpProgram(PROGRAM_SOURCE, placeholder);
        LineString expected = new LineString(ImmutableList.of("foo", "bar", "baz", "", ""));
        assertEquals(expected, actual);
    }

    @Test
    public void dumpProgram_justFitsByHeight() {
        Placeholder placeholder = new Placeholder(PlaceholderKind.CODE, 1, 0, 4, 3);
        LineString actual = dumpProgram(PROGRAM_SOURCE, placeholder);
        LineString expected = new LineString(ImmutableList.of("foo", "bar", "baz"));
        assertEquals(expected, actual);
    }

    @Test
    public void dumpProgram_justFitsByWidth() {
        Placeholder placeholder = new Placeholder(PlaceholderKind.CODE, 1, 0, 3, 5);
        LineString actual = dumpProgram(PROGRAM_SOURCE, placeholder);
        LineString expected = new LineString(ImmutableList.of("foo", "bar", "baz", "", ""));
        assertEquals(expected, actual);
    }

    @Test(expected = ExamTemplateRealizationException.class)
    public void dumpProgram_tooHigh() {
        Placeholder placeholder = new Placeholder(PlaceholderKind.CODE, 1, 0, 5, 2);
        dumpProgram(PROGRAM_SOURCE, placeholder);
    }

    @Test(expected = ExamTemplateRealizationException.class)
    public void dumpProgram_tooWide() {
        Placeholder placeholder = new Placeholder(PlaceholderKind.CODE, 1, 0, 2, 5);
        dumpProgram(PROGRAM_SOURCE, placeholder);
    }
}
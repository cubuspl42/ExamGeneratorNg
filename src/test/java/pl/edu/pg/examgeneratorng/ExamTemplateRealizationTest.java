package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import pl.edu.pg.examgeneratorng.exceptions.ExamTemplateRealizationException;

import static org.junit.Assert.assertEquals;
import static pl.edu.pg.examgeneratorng.ExamTemplateRealization.dumpProgram;
import static pl.edu.pg.examgeneratorng.MatrixPlaceholder.Kind.CODE;

public class ExamTemplateRealizationTest {
    private static final LineString PROGRAM_SOURCE = new LineString(ImmutableList.of("foo", "bar", "baz"));

    @Test
    public void dumpProgram_fits() {
        MatrixPlaceholder placeholder = new MatrixPlaceholder(4, 5, 1, 0, CODE);
        LineString actual = dumpProgram(PROGRAM_SOURCE, placeholder);
        LineString expected = new LineString(ImmutableList.of("foo", "bar", "baz", "", ""));
        assertEquals(expected, actual);
    }

    @Test
    public void dumpProgram_justFitsByHeight() {
        MatrixPlaceholder placeholder = new MatrixPlaceholder(4, 3, 1, 0, CODE);
        LineString actual = dumpProgram(PROGRAM_SOURCE, placeholder);
        LineString expected = new LineString(ImmutableList.of("foo", "bar", "baz"));
        assertEquals(expected, actual);
    }

    @Test
    public void dumpProgram_justFitsByWidth() {
        MatrixPlaceholder placeholder = new MatrixPlaceholder(3, 5, 1, 0, CODE);
        LineString actual = dumpProgram(PROGRAM_SOURCE, placeholder);
        LineString expected = new LineString(ImmutableList.of("foo", "bar", "baz", "", ""));
        assertEquals(expected, actual);
    }

    @Test(expected = ExamTemplateRealizationException.class)
    public void dumpProgram_tooHigh() {
        MatrixPlaceholder placeholder = new MatrixPlaceholder(5, 2, 1, 0, CODE);
        dumpProgram(PROGRAM_SOURCE, placeholder);
    }

    @Test(expected = ExamTemplateRealizationException.class)
    public void dumpProgram_tooWide() {
        MatrixPlaceholder placeholder = new MatrixPlaceholder(2, 5, 1, 0, CODE);
        dumpProgram(PROGRAM_SOURCE, placeholder);
    }
}
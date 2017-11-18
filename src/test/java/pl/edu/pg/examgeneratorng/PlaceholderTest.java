package pl.edu.pg.examgeneratorng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.edu.pg.examgeneratorng.MatrixPlaceholder.Kind.*;

public class PlaceholderTest {
    @Test
    public void parse_code() {
        Placeholder actual = MatrixPlaceholder.parse("#01/01--\\\\");
        Placeholder expected = new MatrixPlaceholder(8, 3, 1, 1, CODE);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_output() {
        Placeholder actual = MatrixPlaceholder.parse("!01/01--\\\\");
        Placeholder expected = new MatrixPlaceholder(8, 3, 1, 1, OUTPUT);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_secretOutput() {
        Placeholder actual = MatrixPlaceholder.parse("?01/01--\\\\");
        Placeholder expected = new MatrixPlaceholder(8, 3, 1, 1, SECRET_OUTPUT);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noMinusesNoBackslashes() {
        Placeholder actual = MatrixPlaceholder.parse("#01/01");
        Placeholder expected = new MatrixPlaceholder(6, 1, 1, 1, CODE);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noLineIndex() {
        Placeholder actual = MatrixPlaceholder.parse("#01--\\\\");
        Placeholder expected = new MatrixPlaceholder(5, 3, 1, 0, CODE);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noMinuses() {
        Placeholder actual = MatrixPlaceholder.parse("#01/01\\\\");
        Placeholder expected = new MatrixPlaceholder(6, 3, 1, 1, CODE);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noBackslashes() {
        Placeholder actual = MatrixPlaceholder.parse("#01/01--");
        Placeholder expected = new MatrixPlaceholder(8, 1, 1, 1, CODE);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_invalidSymbol() {
        MatrixPlaceholder.parse("@01/01--");
    }
}
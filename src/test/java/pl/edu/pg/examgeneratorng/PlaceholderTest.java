package pl.edu.pg.examgeneratorng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaceholderTest {
    @Test
    public void parse_code() {
        Placeholder actual = Placeholder.parse("#01/01--\\\\");
        Placeholder expected = new Placeholder(PlaceholderKind.CODE, 1, 1, 8, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_output() {
        Placeholder actual = Placeholder.parse("!01/01--\\\\");
        Placeholder expected = new Placeholder(PlaceholderKind.OUTPUT, 1, 1, 8, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_secretOutput() {
        Placeholder actual = Placeholder.parse("?01/01--\\\\");
        Placeholder expected = new Placeholder(PlaceholderKind.SECRET_OUTPUT, 1, 1, 8, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noMinusesNoBackslashes() {
        Placeholder actual = Placeholder.parse("#01/01");
        Placeholder expected = new Placeholder(PlaceholderKind.CODE, 1, 1, 6, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noLineIndex() {
        Placeholder actual = Placeholder.parse("#01--\\\\");
        Placeholder expected = new Placeholder(PlaceholderKind.CODE, 1, 0, 5, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noMinuses() {
        Placeholder actual = Placeholder.parse("#01/01\\\\");
        Placeholder expected = new Placeholder(PlaceholderKind.CODE, 1, 1, 6, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void parse_noBackslashes() {
        Placeholder actual = Placeholder.parse("#01/01--");
        Placeholder expected = new Placeholder(PlaceholderKind.CODE, 1, 1, 8, 1);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_invalidSymbol() {
        Placeholder.parse("@01/01--");
    }
}
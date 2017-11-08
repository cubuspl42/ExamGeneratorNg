package pl.edu.pg.examgeneratorng;

import org.junit.Test;

import static org.junit.Assert.*;

public class GroupTest {
    @Test
    public void fromLowercaseIdentifier_a() {
        Group actual = Group.fromLowercaseIdentifier("a");
        Group expected = new Group(0);
        assertEquals(expected, actual);
    }

    @Test
    public void fromLowercaseIdentifier_b() {
        Group actual = Group.fromLowercaseIdentifier("b");
        Group expected = new Group(1);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromLowercaseIdentifier_0() {
        Group.fromLowercaseIdentifier("0");
    }
}
package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import pl.edu.pg.examgeneratorng.LineTemplate.LineKind;

import static org.junit.Assert.assertEquals;
import static pl.edu.pg.examgeneratorng.LineTemplateUtils.lineTemplate;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeLineTemplate;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;

public class ProgramTemplateRealizationTest {
    @Test
    public void realizeProgramTemplate_simple() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                lineTemplate("foo"),
                lineTemplate("bar")
        ));

        LineString actual = realizeProgramTemplate(programTemplate, Group.A, ProgramVariant.STUDENT);

        LineString expected = new LineString(ImmutableList.of("foo", "bar"));

        assertEquals(expected, actual);
    }


    @Test
    public void realizeProgramTemplate_gap() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(
                        ImmutableList.of(new LineTemplate.GapNode("foo")),
                        LineKind.NORMAL
                )
        ));

        LineString actual = realizeProgramTemplate(programTemplate, Group.A, ProgramVariant.STUDENT);

        LineString expected = LineString.fromSingleLine("___");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentText() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.NORMAL
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.STUDENT);

        String expected = "foo";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentGap() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.NORMAL
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.STUDENT);

        String expected = "___";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherText() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.NORMAL
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.TEACHER);

        String expected = "foo";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherGap() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.NORMAL
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.TEACHER);

        String expected = "foo";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentExcluded() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.EXCLUDED
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.STUDENT);

        String expected = "foo";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherExcluded() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.EXCLUDED
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.TEACHER);

        String expected = "foo";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentHidden() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.HIDDEN
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.STUDENT);

        String expected = "";

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherHidden() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.HIDDEN
        );

        String actual = realizeLineTemplate(lineTemplate, Group.A, ProgramVariant.TEACHER);

        String expected = "";

        assertEquals(expected, actual);
    }

}
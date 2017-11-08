package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import pl.edu.pg.examgeneratorng.LineTemplate.LineKind;

import java.util.Optional;

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

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.STUDENT, Group.A);

        LineString expected = new LineString(ImmutableList.of("foo", "bar"));

        assertEquals(expected, actual);
    }


    @Test
    public void realizeProgramTemplate_gapStudent() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(
                        ImmutableList.of(new LineTemplate.GapNode("foo")),
                        LineKind.NORMAL,
                        null
                )
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.STUDENT, Group.A);

        LineString expected = LineString.fromSingleLine("___");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_hiddenStudent() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.HIDDEN),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL)
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.STUDENT, Group.A);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_hiddenTeacher() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.HIDDEN),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL)
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.TEACHER, Group.A);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_hiddenCompiler() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.HIDDEN),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL)
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, Group.A);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "bar",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_excludedStudent() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL ),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.EXCLUDED ),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL )
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.STUDENT, Group.A);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "bar",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_excludedTeacher() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.EXCLUDED),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL)
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.TEACHER, Group.A);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "bar",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_excludedCompiler() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.EXCLUDED),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL)
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, Group.A);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeProgramTemplate_groupDifferent() throws Exception {
        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("foo")), LineKind.NORMAL),
                new LineTemplate(
                        ImmutableList.of(new LineTemplate.TextNode("bar")), LineKind.EXCLUDED, Group.A),
                new LineTemplate(ImmutableList.of(new LineTemplate.TextNode("baz")), LineKind.NORMAL)
        ));

        LineString actual = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, Group.B);

        LineString expected = new LineString(ImmutableList.of(
                "foo",
                "baz"
        ));

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentText() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.NORMAL
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.STUDENT, Group.A);

        Optional<String> expected = Optional.of("foo");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentGap() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.NORMAL
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.STUDENT, Group.A);

        Optional<String> expected = Optional.of("___");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherText() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.NORMAL
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.TEACHER, Group.A);

        Optional<String> expected = Optional.of("foo");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherGap() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.NORMAL
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.TEACHER, Group.A);

        Optional<String> expected = Optional.of("foo");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentExcluded() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.EXCLUDED
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.STUDENT, Group.A);

        Optional<String> expected = Optional.of("foo");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherExcluded() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.EXCLUDED
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.TEACHER, Group.A);

        Optional<String> expected = Optional.of("foo");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_studentHidden() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.HIDDEN
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.STUDENT, Group.A);

        Optional<String> expected = Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_teacherHidden() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.GapNode("foo")),
                LineKind.HIDDEN
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.TEACHER, Group.A);

        Optional<String> expected = Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_groupSame() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.NORMAL,
                Group.A
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.STUDENT, Group.A);

        Optional<String> expected = Optional.of("foo");

        assertEquals(expected, actual);
    }

    @Test
    public void realizeLineTemplate_groupDifferent() {
        LineTemplate lineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("foo")),
                LineKind.NORMAL,
                Group.B
        );

        Optional<String> actual = realizeLineTemplate(lineTemplate, ProgramVariant.STUDENT, Group.A);

        Optional<String> expected = Optional.empty();

        assertEquals(expected, actual);
    }
}
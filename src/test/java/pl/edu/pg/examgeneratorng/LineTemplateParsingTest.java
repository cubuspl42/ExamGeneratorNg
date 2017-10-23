package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
import static pl.edu.pg.examgeneratorng.LineTemplateParsing.parseLineTemplate;

public class LineTemplateParsingTest {
    @Test
    public void test_parseLineTemplate_text() {
        String lineStr = "int tab[3][3], i, j, na=0, wynik[3][3];";

        LineTemplate lineTemplate = parseLineTemplate(lineStr);

        LineTemplate expectedLineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode(lineStr)),
                LineTemplate.LineKind.NORMAL
        );

        assertEquals(expectedLineTemplate, lineTemplate);
    }

    @Test
    public void test_parseLineTemplate_singleGap() {
        String lineStr = "tab[i][j] = __i__+na;";

        LineTemplate lineTemplate = parseLineTemplate(lineStr);

        LineTemplate expectedLineTemplate = new LineTemplate(
                ImmutableList.of(
                        new LineTemplate.TextNode("tab[i][j] = "),
                        new LineTemplate.GapNode("i"),
                        new LineTemplate.TextNode("+na;")
                ),
                LineTemplate.LineKind.NORMAL
        );

        assertEquals(expectedLineTemplate, lineTemplate);
    }

    @Test
    public void test_parseLineTemplate_multipleGaps() {
        String lineStr = "tab[i][j] = __i__+__na__;";

        LineTemplate lineTemplate = parseLineTemplate(lineStr);

        LineTemplate expectedLineTemplate = new LineTemplate(
                ImmutableList.of(
                        new LineTemplate.TextNode("tab[i][j] = "),
                        new LineTemplate.GapNode("i"),
                        new LineTemplate.TextNode("+"),
                        new LineTemplate.GapNode("na"),
                        new LineTemplate.TextNode(";")
                ),
                LineTemplate.LineKind.NORMAL
        );

        assertEquals(expectedLineTemplate, lineTemplate);
    }

    @Test
    public void test_parseLineTemplate_excluded() {
        String lineStr = "  cout << &i << endl; %excluded";

        LineTemplate lineTemplate = parseLineTemplate(lineStr);

        LineTemplate expectedLineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode("  cout << &i << endl;")),
                LineTemplate.LineKind.EXCLUDED
        );

        assertEquals(expectedLineTemplate, lineTemplate);
    }

    @Test
    public void test_parseLineTemplate_hidden() {
        String lineStr = "  cout << \"BŁĄD\" << endl; %hidden";

        LineTemplate lineTemplate = parseLineTemplate(lineStr);

        LineTemplate expectedLineTemplate = new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode( "  cout << \"BŁĄD\" << endl;")),
                LineTemplate.LineKind.HIDDEN
        );

        assertEquals(expectedLineTemplate, lineTemplate);
    }
}
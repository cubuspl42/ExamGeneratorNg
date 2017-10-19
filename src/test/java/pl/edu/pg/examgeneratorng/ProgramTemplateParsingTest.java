package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgramTemplateParsingTest {
    private static final String PROGRAM1 = "" +
            "#include <iostream>\n" +
            "\n" +
            "using namespace std;\n" +
            "\n" +
            "%%\n" +
            "\n" +
            "int main() {\n" +
            "    cout << 1 << endl;\n" +
            "}";

    @Test
    public void parseProgramTemplate_simple() throws Exception {
        ProgramTemplate actual = ProgramTemplateParsing.parseProgramTemplate(PROGRAM1);

        ProgramTemplate expected = new ProgramTemplate(
                ImmutableList.of(
                        "#include <iostream>",
                        "",
                        "using namespace std;",
                        ""
                ),
                ImmutableList.of(
                        normalLine(""),
                        normalLine("int main() {"),
                        normalLine("cout << 1 << endl;"),
                        normalLine("}")
                )
        );

        assertEquals(expected, actual);
    }

    private ProgramTemplate.LineNode normalLine(String textContent) {
        return new ProgramTemplate.LineNode(
                new LineTemplate(
                        ImmutableList.of(new LineTemplate.TextNode(textContent)),
                        LineTemplate.LineKind.NORMAL
                )
        );
    }
}

package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.edu.pg.examgeneratorng.ProgramTemplateParsing.parseProgramTemplate;

public class ProgramTemplateParsingTest {
    private static final String PROGRAM1 = "" +
            "#include <iostream>\n" +
            "int main() {\n" +
            "    std::cout << 1 << std::endl;\n" +
            "}";

    @Test
    public void parseProgramTemplate_simple() throws Exception {
        ProgramTemplate actual = parseProgramTemplate(PROGRAM1);

        ProgramTemplate expected = new ProgramTemplate(
                ImmutableList.of(
                        lineNode("#include <iostream>"),
                        lineNode("int main() {"),
                        lineNode("    std::cout << 1 << std::endl;"),
                        lineNode("}")
                )
        );

        assertEquals(expected, actual);
    }

    private ProgramTemplate.LineNode lineNode(String textContent) {
        return new ProgramTemplate.LineNode(
                new LineTemplate(
                        ImmutableList.of(new LineTemplate.TextNode(textContent)),
                        LineTemplate.LineKind.NORMAL
                )
        );
    }
}

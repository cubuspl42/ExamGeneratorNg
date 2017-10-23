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
                        LineTemplateUtils.lineTemplate("#include <iostream>"),
                        LineTemplateUtils.lineTemplate("int main() {"),
                        LineTemplateUtils.lineTemplate("    std::cout << 1 << std::endl;"),
                        LineTemplateUtils.lineTemplate("}")
                )
        );

        assertEquals(expected, actual);
    }

}

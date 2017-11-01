package pl.edu.pg.examgeneratorng;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

import static java.util.Arrays.asList;

public class GppCppProgramEvaluatorTest {

    @Test
    public void evaluate_output() {

        List<String> expectedResult = asList("test test test");
        String sourceCode =
                "#include <stdio.h>\n" +
                "\n" +
                "int main(void) {\n" +
                "\n" +
                "   printf(\"" + expectedResult.get(0) +"\");\n" +
                "   return 0;\n" +
                "}\n";

        GppCppProgramEvaluator gppCppProgramEvaluator = new GppCppProgramEvaluator();
        ProgramOutput output = gppCppProgramEvaluator.evaluate(sourceCode);
        List<String> result = output.getLines();

        assertEquals(expectedResult, result);
    }
}

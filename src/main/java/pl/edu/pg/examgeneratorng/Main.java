package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;

import java.io.File;
import java.util.List;

public class Main {
    private static final String CODE = "" +
            "int ______________ = {{{1, 2}, {1, 2}}, {{1, 2}, {1, 2}}};\n" +
            "\n" +
            "int f(int t[___________], int n)\n" +
            "{\n" +
            "    int i, j, k, s = 7;\n" +
            "    for (i = 0; i <= n; i++)\n" +
            "        for (j = n; j >= 0; j--)\n" +
            "            for (k = 0; k <= n; k++)\n" +
            "                s += t[i][j][k];\n" +
            "    return _____________;\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "    cout << f(g, _________);\n" +
            "    return 0;\n" +
            "}";

    private static final ImmutableList<String> OUTPUT = ImmutableList.of("foo", "bar", "baz", "baz");

    public static void main(String[] args) throws Exception {
        String documentPath = args[0];
        String outputPath = args[1];

        OdfTextDocument odt = (OdfTextDocument) OdfDocument.loadDocument(documentPath);
        OdfContentDom contentDom = odt.getContentDom();

        List<Placeholder> placeholders = PlaceholderUtils.findPlaceholders(contentDom.getRootElement());

        ExamProgram program = ExamProgram.builder()
                .source(CODE)
                .output(OUTPUT)
                .build();

        Exam exam = Exam.builder()
                .programs(ImmutableList.of(program, program, program))
                .build();

        PlaceholderUtils.fillExamPlaceholders(contentDom, placeholders, exam);

        odt.save(new File(outputPath));
    }
}

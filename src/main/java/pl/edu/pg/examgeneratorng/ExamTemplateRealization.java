package pl.edu.pg.examgeneratorng;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextSElement;
import org.odftoolkit.odfdom.incubator.doc.text.OdfTextParagraph;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import pl.edu.pg.examgeneratorng.exceptions.ExamTemplateRealizationException;
import pl.edu.pg.examgeneratorng.util.DomUtils;
import pl.edu.pg.examgeneratorng.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.nCopies;
import static pl.edu.pg.examgeneratorng.util.DomUtils.appendChildren;
import static pl.edu.pg.examgeneratorng.util.ListUtils.concat;
import static pl.edu.pg.examgeneratorng.util.StringUtils.nCopiesOfChar;

final class ExamTemplateRealization {
    static void fillPlaceholders(
            OdfContentDom contentDom, List<PlaceholderRef> placeholderRefs, Exam exam, ExamVariant variant) {
        placeholderRefs.forEach(placeholderRef -> fillPlaceholder(contentDom, placeholderRef, exam, variant));
    }

    private static void fillPlaceholder(
            OdfContentDom contentDom, PlaceholderRef placeholderRef, Exam exam, ExamVariant variant) {
        Placeholder placeholder = placeholderRef.getPlaceholder();
        ExamProgram program = exam.getExamProgramMap().get(new ProgramId(placeholder.getIndex()));
        LineString content = extractContentForPlaceholder(program, placeholder, variant);
        fillPlaceholderWithContent(contentDom, placeholderRef, content);
    }

    private static void fillPlaceholderWithContent(
            OdfContentDom contentDom, PlaceholderRef placeholderRef, LineString content) {
        TableTableCellElement wrapperCell = placeholderRef.getWrapperCell();
        String styleName = placeholderRef.getStyleName();

        DomUtils.removeAllChildren(wrapperCell);

        OdfTextParagraph codeParagraph = lineStringToParagraph(content, contentDom, styleName);
        wrapperCell.appendChild(codeParagraph);
    }

    private static OdfTextParagraph lineStringToParagraph(LineString str, OdfContentDom contentDom, String styleName) {
        List<List<Node>> nodesMatrix = str.getLines().stream()
                .map(line -> lineToNodes(contentDom, line))
                .collect(Collectors.toList());

        List<Node> nodes = ListUtils.join(nodesMatrix, () -> ImmutableList.of(new TextLineBreakElement(contentDom)));

        OdfTextParagraph paragraph = new OdfTextParagraph(contentDom, styleName);
        appendChildren(paragraph, nodes);

        return paragraph;
    }

    private static List<Node> lineToNodes(OdfContentDom contentDom, String line) {
        List<Node> nodes = new ArrayList<>();

        line.chars()
                .mapToObj(c -> (char) c)
                .forEach(character -> combineNodesWithChar(contentDom, nodes, character));

        return nodes;
    }

    private static void combineNodesWithChar(OdfContentDom contentDom, List<Node> nodes, Character character) {
        Node last = nodes.isEmpty() ? null : nodes.get(nodes.size() - 1);
        if (character == ' ' && last instanceof Text) {
            Text text = (Text) last;
            String textContent = text.getData();
            if (textContent.charAt(textContent.length() - 1) == ' ') {
                TextSElement s = new TextSElement(contentDom);
                s.setTextCAttribute(1);
                nodes.add(s);
            } else {
                text.setData(textContent + character);
            }
        } else if (character == ' ' && last instanceof TextSElement) {
            TextSElement s = (TextSElement) last;
            s.setTextCAttribute(s.getTextCAttribute() + 1);
        } else if (character != ' ' && last instanceof Text) {
            Text text = (Text) last;
            text.setData(text.getData() + character);
        } else {
            Text text = contentDom.createTextNode(character.toString());
            nodes.add(text);
        }
    }

    private static LineString extractContentForPlaceholder(
            ExamProgram program, Placeholder placeholder, ExamVariant variant) {
        switch (placeholder.getKind()) {
            case CODE:
                return dumpProgram(program.getSource(), placeholder);
            case OUTPUT:
                return dumpOutput(program.getOutput(), placeholder);
            case SECRET_OUTPUT:
                return dumpSecretOutput(program.getOutput(), placeholder, variant);
        }
        throw new AssertionError();
    }

    static LineString dumpProgram(LineString programSource, Placeholder placeholder) {
        Preconditions.checkArgument(placeholder.getKind() == PlaceholderKind.CODE);
        List<String> programLines = programSource.getLines();
        if (programLines.size() > placeholder.getHeight()) {
            throw new ExamTemplateRealizationException("Program is too high for placeholder " + placeholder.repr());
        } else if (programSource.longestLineLength() > placeholder.getWidth()) {
            throw new ExamTemplateRealizationException("Program is too wide for placeholder " + placeholder.repr());
        } else {
            int n = placeholder.getHeight() - programLines.size();
            return new LineString(concat(programLines, nCopies(n, "")));
        }
    }

    private static LineString dumpOutput(LineString programOutput, Placeholder placeholder) {
        Preconditions.checkArgument(placeholder.getKind() == PlaceholderKind.OUTPUT);
        return dumpOutputLineString(programOutput, placeholder);
    }

    private static LineString dumpOutputLineString(LineString programOutput, Placeholder placeholder) {
        int lineIndex = placeholder.getLineIndex() - 1;
        List<String> programOutputLines = programOutput.getLines();
        if (lineIndex < programOutputLines.size()) {
            return LineString.fromSingleLine(programOutputLines.get(lineIndex));
        } else {
            throw new ExamTemplateRealizationException(placeholder.repr() + ": Index out of bounds of program output");
        }
    }

    private static LineString dumpSecretOutput(
            LineString programOutput, Placeholder placeholder, ExamVariant variant) {
        Preconditions.checkArgument(placeholder.getKind() == PlaceholderKind.SECRET_OUTPUT);
        if (variant == ExamVariant.STUDENT) {
            return LineString.fromSingleLine(nCopiesOfChar(placeholder.getWidth(), '_'));
        } else if (variant == ExamVariant.TEACHER) {
            return dumpOutputLineString(programOutput, placeholder);
        }
        throw new AssertionError();
    }
}

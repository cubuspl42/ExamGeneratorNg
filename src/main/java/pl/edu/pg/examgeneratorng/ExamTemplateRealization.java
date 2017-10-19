package pl.edu.pg.examgeneratorng;

import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextSElement;
import org.odftoolkit.odfdom.incubator.doc.text.OdfTextParagraph;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import pl.edu.pg.examgeneratorng.util.DomUtils;
import pl.edu.pg.examgeneratorng.util.ListUtils;
import pl.edu.pg.examgeneratorng.util.StringUtils;

import java.util.List;

import static java.lang.Integer.parseInt;
import static pl.edu.pg.examgeneratorng.util.DomUtils.appendChildren;

final class ExamTemplateRealization {
    static void fillPlaceholders(
            OdfContentDom contentDom, List<Placeholder> placeholders, Exam exam, ExamVariant variant) {
        placeholders.forEach(placeholder -> fillPlaceholder(contentDom, placeholder, exam, variant));
    }

    private static void fillPlaceholder(
            OdfContentDom contentDom, Placeholder placeholder, Exam exam, ExamVariant variant) {
        ExamProgram program = exam.getPrograms().get(placeholder.getIndex() - 1);
        String content = extractContentForPlaceholder(program, placeholder, variant);
        fillPlaceholderWithContent(contentDom, placeholder, content);
    }

    private static void fillPlaceholderWithContent(OdfContentDom contentDom, Placeholder placeholder, String content) {
        TableTableCellElement wrapperCell = placeholder.getWrapperCell();
        String styleName = placeholder.getStyleName();

        DomUtils.removeAllChildren(wrapperCell);

        OdfTextParagraph codeParagraph = stringToParagraph(content, contentDom, styleName);
        wrapperCell.appendChild(codeParagraph);
    }

    private static OdfTextParagraph stringToParagraph(String str, OdfContentDom contentDom, String styleName) {
        List<Node> children = str
                .chars()
                .mapToObj(c -> (char) c)
                .reduce(ListUtils.emptyMutableList(),
                        (nodes, character) -> combineNodesWithChar(contentDom, nodes, character),
                        ListUtils::concat);

        OdfTextParagraph paragraph = new OdfTextParagraph(contentDom, styleName);
        appendChildren(paragraph, children);

        return paragraph;
    }

    private static List<Node> combineNodesWithChar(OdfContentDom contentDom, List<Node> nodes, Character character) {
        Node last = nodes.isEmpty() ? null : nodes.get(nodes.size() - 1);
        if (character == '\n') {
            TextLineBreakElement lineBreak = new TextLineBreakElement(contentDom);
            nodes.add(lineBreak);
        } else if (character == ' ' && last instanceof Text) {
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
        return nodes;
    }

    private static String extractContentForPlaceholder(
            ExamProgram program, Placeholder placeholder, ExamVariant variant) {
        switch (placeholder.getKind()) {
            case CODE:
                return program.getSource();
            case OUTPUT:
                return extractContentForOutputPlaceholder(program, placeholder);
            case SECRET_OUTPUT:
                if (variant == ExamVariant.STUDENT) {
                    return StringUtils.nCopiesOfChar(placeholder.getWidth(), '_');
                } else if (variant == ExamVariant.TEACHER) {
                    return extractContentForOutputPlaceholder(program, placeholder);
                }
        }
        throw new AssertionError();
    }

    private static String extractContentForOutputPlaceholder(ExamProgram program, Placeholder placeholder) {
        return program.getOutput().getLines().get(placeholder.getLineIndex() - 1);
    }
}
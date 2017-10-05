package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSElement;
import org.odftoolkit.odfdom.incubator.doc.text.OdfTextParagraph;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import pl.edu.pg.examgeneratorng.util.DomUtils;
import pl.edu.pg.examgeneratorng.util.ListUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

class Placeholders {
    private static final String CODE = "" +
            "void param( l_t p0, l_t *p1, l_t **p2, l_t p3[] )\n" +
            "{\n" +
            "    p0.x = 3;\n" +
            "    (*p1).x = 2;\n" +
            "    p1++;\n" +
            "    p2++;\n" +
            "    p3->x = 4;\n" +
            "}";

    private static final int CODE_PLACEHOLDER_PREFIX_LENGTH = "#00".length();

    private static final Pattern CODE_PLACEHOLDER_PATTERN = Pattern.compile("#(\\d\\d)(-)+(\\\\)+");

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

    private static void appendChildren(Node parent, List<Node> children) {
        children.forEach(parent::appendChild);
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

    static List<CodePlaceholder> findPlaceholders(Node node) {
        Node firstChild = node.getFirstChild();
        String textContent = node.getTextContent();
        Matcher matcher = CODE_PLACEHOLDER_PATTERN.matcher(textContent);

        if (matcher.matches()
                && node instanceof TableTableCellElement
                && firstChild instanceof TextPElement) {

            TableTableCellElement wrapperCell = (TableTableCellElement) node;
            TextPElement firstParagraph = (TextPElement) firstChild;
            String firstParagraphStyleName = firstParagraph.getStyleName();

            int id = parseInt(matcher.group(1));
            int width = calculatePlaceholderWidth(matcher);
            int height = calculatePlaceholderHeight(matcher);
            CodePlaceholder placeholder = new CodePlaceholder(wrapperCell, firstParagraphStyleName, id, width, height);
            return ImmutableList.of(placeholder);
        }

        return DomUtils.getChildren(node).stream()
                .flatMap(child -> findPlaceholders(child).stream())
                .collect(Collectors.toList());
    }

    private static int calculatePlaceholderWidth(Matcher matcher) {
        return CODE_PLACEHOLDER_PREFIX_LENGTH + matcher.group(2).length();
    }

    private static int calculatePlaceholderHeight(Matcher matcher) {
        return 1 + matcher.group(3).length();
    }

    private static void fillPlaceholder(OdfContentDom contentDom, CodePlaceholder placeholder) {
        TableTableCellElement wrapperCell = placeholder.getWrapperCell();
        String styleName = placeholder.getFirstParagraphStyleName();

        DomUtils.removeAllChildren(wrapperCell);

        OdfTextParagraph codeParagraph = stringToParagraph(CODE, contentDom, styleName);
        wrapperCell.appendChild(codeParagraph);
    }

    static void fillPlaceholders(OdfContentDom contentDom, List<CodePlaceholder> placeholders) {
        placeholders.forEach(placeholder -> fillPlaceholder(contentDom, placeholder));
    }
}

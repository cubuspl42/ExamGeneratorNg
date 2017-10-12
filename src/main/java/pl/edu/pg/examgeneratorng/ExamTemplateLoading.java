package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.w3c.dom.Node;
import pl.edu.pg.examgeneratorng.util.DomUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

final class ExamTemplateLoading {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([#!?])(\\d\\d)(/\\d\\d)?(-*)(\\\\)*");

    static List<Placeholder> findPlaceholders(Node node) {
        Node firstChild = node.getFirstChild();
        String textContent = node.getTextContent();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(textContent);

        if (matcher.matches()
                && node instanceof TableTableCellElement
                && firstChild instanceof TextPElement) {

            TableTableCellElement wrapperCell = (TableTableCellElement) node;
            TextPElement firstParagraph = (TextPElement) firstChild;
            String firstParagraphStyleName = firstParagraph.getStyleName();

            String kindStr = matcher.group(1);
            String indexStr = matcher.group(2);
            String lineIndexStr = matcher.group(3);
            String minusesStr = matcher.group(4);
            String slashesStr = matcher.group(5);

            PlaceholderKind kind = charToPlaceholderKind(kindStr);
            int index = parseInt(indexStr);
            int lineIndex = lineIndexStr != null ? parseInt(lineIndexStr.substring(1)) : 0;
            int width = indexStr.length() + (lineIndexStr != null ? lineIndexStr.length() : 0) + minusesStr.length();
            int height = 1 + (slashesStr != null ? slashesStr.length() : 0);

            Placeholder placeholder = new Placeholder(
                    wrapperCell, firstParagraphStyleName, kind, index, lineIndex, width, height);

            return ImmutableList.of(placeholder);
        }

        return DomUtils.getChildren(node).stream()
                .flatMap(child -> findPlaceholders(child).stream())
                .collect(Collectors.toList());
    }

    private static PlaceholderKind charToPlaceholderKind(String kindStr) {
        char c = kindStr.charAt(0);
        switch (c) {
            case '#':
                return PlaceholderKind.CODE;
            case '!':
                return PlaceholderKind.OUTPUT;
            case '?':
                return PlaceholderKind.SECRET_OUTPUT;
        }
        throw new IllegalArgumentException(kindStr);
    }

}

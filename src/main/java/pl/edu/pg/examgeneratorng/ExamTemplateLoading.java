package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.w3c.dom.Node;
import pl.edu.pg.examgeneratorng.util.DomUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.Placeholder.PLACEHOLDER_PATTERN;

final class ExamTemplateLoading {

    static List<PlaceholderRef> findPlaceholders(Node node) {
        Node firstChild = node.getFirstChild();
        String textContent = node.getTextContent();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(textContent);

        if (matcher.matches()
                && node instanceof TableTableCellElement
                && firstChild instanceof TextPElement) {

            TableTableCellElement wrapperCell = (TableTableCellElement) node;
            TextPElement firstParagraph = (TextPElement) firstChild;
            String firstParagraphStyleName = firstParagraph.getStyleName();

            Placeholder placeholder = Placeholder.parse(textContent);

            PlaceholderRef placeholderRef = new PlaceholderRef(
                    wrapperCell, firstParagraphStyleName, placeholder);

            return ImmutableList.of(placeholderRef);
        }

        return DomUtils.getChildren(node).stream()
                .flatMap(child -> findPlaceholders(child).stream())
                .collect(Collectors.toList());
    }

}

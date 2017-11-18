package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.w3c.dom.Node;
import pl.edu.pg.examgeneratorng.util.DomUtils;

import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.LinePlaceholder.isLinePlaceholder;
import static pl.edu.pg.examgeneratorng.MatrixPlaceholder.isMatrixPlaceholder;

final class ExamTemplateLoading {

    static List<PlaceholderRef> findPlaceholders(Node node) {
        if (isNotLineOrMatrixPlaceholder(node))
            return DomUtils.getChildren(node).stream()
                    .flatMap(child -> findPlaceholders(child).stream())
                    .collect(Collectors.toList());

        Node firstChild = node.getFirstChild();
        String textContent = node.getTextContent();

        TableTableCellElement wrapperCell = (TableTableCellElement) node;
        TextPElement firstParagraph = (TextPElement) firstChild;
        String firstParagraphStyleName = firstParagraph.getStyleName();

        Placeholder placeholder = isMatrixPlaceholder(textContent) ?
                MatrixPlaceholder.parse(textContent) : LinePlaceholder.parse(textContent);

        PlaceholderRef placeholderRef = new PlaceholderRef(wrapperCell, firstParagraphStyleName, placeholder);

        return ImmutableList.of(placeholderRef);
    }

    private static boolean isNotLineOrMatrixPlaceholder(Node node) {

        return !((isMatrixPlaceholder(node.getTextContent()) || isLinePlaceholder(node.getTextContent()))
                && node instanceof TableTableCellElement
                && node.getFirstChild() instanceof TextPElement);
    }
}

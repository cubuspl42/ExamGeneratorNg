package pl.edu.pg.examgeneratorng;

import lombok.Value;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;

@Value
class Placeholder {
    private TableTableCellElement wrapperCell;
    private String styleName;
    private PlaceholderKind kind;
    private int index;
    private int lineIndex;
    private int width;
    private int height;
}

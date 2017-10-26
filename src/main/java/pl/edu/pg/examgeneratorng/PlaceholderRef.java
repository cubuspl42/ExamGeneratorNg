package pl.edu.pg.examgeneratorng;

import lombok.Value;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;

@Value
class PlaceholderRef {
    private TableTableCellElement wrapperCell;
    private String styleName;
    Placeholder placeholder;
}

package pl.edu.pg.examgeneratorng;

import lombok.Value;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;

@Value
class CodePlaceholder {
    private TableTableCellElement wrapperCell;
    private String firstParagraphStyleName;
    private int id;
    private int width;
    private int height;
}

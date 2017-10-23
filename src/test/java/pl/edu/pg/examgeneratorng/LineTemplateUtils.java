package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;

class LineTemplateUtils {
    static LineTemplate lineTemplate(String textContent) {
        return new LineTemplate(
                ImmutableList.of(new LineTemplate.TextNode(textContent)),
                LineTemplate.LineKind.NORMAL
        );
    }
}

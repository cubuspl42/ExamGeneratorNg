package pl.edu.pg.examgeneratorng;

import lombok.Value;

import java.util.List;

@Value
class LineTemplate {
    enum LineKind {
        NORMAL,
        HIDDEN,
        EXCLUDED
    }

    interface Node {
    }

    @Value
    static class TextNode implements Node {
        private String content;
    }

    @Value
    static class GapNode implements Node {
        private String content;
    }

    private List<Node> nodes;

    private LineKind kind;
}

package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;
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

    LineTemplate(List<Node> nodes, LineKind kind, @Nullable Group group) {
        this.nodes = nodes;
        this.kind = kind;
        this.group = group;
    }

    LineTemplate(List<Node> nodes, LineKind kind) {
        this(nodes, kind, null);
    }

    @NonNull
    private List<Node> nodes;

    @NonNull
    private LineKind kind;

    @Nullable
    private Group group;
}

package pl.edu.pg.examgeneratorng;

import lombok.Value;

import java.util.List;

@Value
class ProgramTemplate {
    interface Node {
    }

    @Value
    static class LineNode implements Node {
        private LineTemplate lineTemplate;
    }

    @Value
    static class PergroupNode implements Node {
        private List<LineTemplate> lineTemplates;
    }

    private List<String> prologueLines;

    private List<? extends Node> nodes;
}

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

    private List<? extends Node> nodes;
}

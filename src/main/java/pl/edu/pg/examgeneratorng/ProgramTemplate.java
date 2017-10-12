package pl.edu.pg.examgeneratorng;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
class ProgramTemplate {
    static abstract class Marker {
    }

    static class PrologMarker extends Marker {
    }

    @Getter
    @AllArgsConstructor
    static class GroupMarker extends Marker {
        private Group group;
    }

    @Value
    static class Line {
        private String content;
        private Set<Marker> markers;
    }

    private List<Line> lines;
}

package pl.edu.pg.examgeneratorng;

import lombok.Value;
import pl.edu.pg.examgeneratorng.util.ListUtils;
import pl.edu.pg.examgeneratorng.util.RegexUtils;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class LineTemplateParsing {
    private static final String GAP_MARKER = "__";
    private static final Pattern GAP_PATTERN = Pattern.compile(GAP_MARKER + "([^_]+)" + GAP_MARKER);

    @Value
    private static class Range {
        private int begin;
        private int end;
    }

    static LineTemplate parseLineTemplate(String lineStr) {
        List<MatchResult> matchResults = RegexUtils.findAll(lineStr, GAP_PATTERN);

        List<Range> ranges = matchResults.stream()
                .map(matchResult -> new Range(matchResult.start(), matchResult.end()))
                .collect(Collectors.toList());

        List<LineTemplate.Node> nodes = ListUtils.mapPath(
                ranges,
                v -> new LineTemplate.GapNode(extractGapContent(lineStr, v)),
                (v0, v1) -> new LineTemplate.TextNode(extractTextContent(lineStr, v0, v1)),
                new Range(-1, 0),
                new Range(lineStr.length(), -1)
        );

        return new LineTemplate(
                nodes,
                LineTemplate.LineKind.NORMAL
        );
    }

    private static String extractGapContent(String lineStr, Range v) {
        int n = GAP_MARKER.length();
        return lineStr.substring(v.getBegin() + n, v.getEnd() - n);
    }

    private static String extractTextContent(String lineStr, Range v0, Range v1) {
        return lineStr.substring(v0.getEnd(), v1.getBegin());
    }
}

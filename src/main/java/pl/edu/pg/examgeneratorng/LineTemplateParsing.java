package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;
import pl.edu.pg.examgeneratorng.LineTemplate.LineKind;
import pl.edu.pg.examgeneratorng.util.ListUtils;
import pl.edu.pg.examgeneratorng.util.RegexUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class LineTemplateParsing {
    private static final String GAP_MARKER = "__";
    private static final Pattern GAP_PATTERN = Pattern.compile(GAP_MARKER + "([^_]+)" + GAP_MARKER);
    private static final Pattern EXCLUDED_PATTERN = Pattern.compile("\\s*%excluded\\s*$");
    private static final Pattern HIDDEN_PATTERN = Pattern.compile("\\s*%hidden\\s*$");

    @Value
    private static class Range {
        private int begin;
        private int end;
    }

    @Value
    private static class NormalizedLine {
        private String content;
        private LineKind kind;
    }

    static LineTemplate parseLineTemplate(String lineStr) {
        NormalizedLine normalizedLine = normalizeLine(lineStr);
        List<LineTemplate.Node> nodes = parseNormalizedLine(normalizedLine.getContent());
        return new LineTemplate(nodes, normalizedLine.getKind());
    }

    private static NormalizedLine normalizeLine(String lineStr) {
        String s0 = RegexUtils.removeFirst(lineStr, EXCLUDED_PATTERN);
        if (!Objects.equals(s0, lineStr)) {
            return new NormalizedLine(s0, LineKind.EXCLUDED);
        }
        String s1 = RegexUtils.removeFirst(lineStr, HIDDEN_PATTERN);
        if (!Objects.equals(s1, lineStr)) {
            return new NormalizedLine(s1, LineKind.HIDDEN);
        }
        return new NormalizedLine(lineStr, LineKind.NORMAL);
    }

    private static List<LineTemplate.Node> parseNormalizedLine(String lineStr) {
        List<MatchResult> matchResults = RegexUtils.findAll(lineStr, GAP_PATTERN);

        List<Range> ranges = matchResults.stream()
                .map(matchResult -> new Range(matchResult.start(), matchResult.end()))
                .collect(Collectors.toList());

        return ListUtils.mapPath(
                ranges,
                v -> new LineTemplate.GapNode(extractGapContent(lineStr, v)),
                (v0, v1) -> new LineTemplate.TextNode(extractTextContent(lineStr, v0, v1)),
                new Range(-1, 0),
                new Range(lineStr.length(), -1)
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

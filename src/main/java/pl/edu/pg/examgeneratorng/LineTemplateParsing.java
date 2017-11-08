package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;
import pl.edu.pg.examgeneratorng.LineTemplate.LineKind;
import pl.edu.pg.examgeneratorng.util.ListUtils;
import pl.edu.pg.examgeneratorng.util.RegexUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class LineTemplateParsing {
    private static final String GAP_MARKER = "__";
    private static final Pattern GAP_PATTERN = Pattern.compile(GAP_MARKER + "([^_]+)" + GAP_MARKER);
    private static final Pattern GROUP_PATTERN = Pattern.compile("\\s*%([a-z])\\s*$");
    private static final Pattern EXCLUDED_PATTERN = Pattern.compile("\\s*%excluded\\s*$");
    private static final Pattern HIDDEN_PATTERN = Pattern.compile("\\s*%hidden\\s*$");

    @Value
    private static class Range {
        private int begin;
        private int end;
    }

    @Value
    private static class NormalizedLine {
        @NonNull
        private String content;
        @NonNull
        private LineKind kind;
        @Nullable
        private Group group;
    }

    static LineTemplate parseLineTemplate(String lineStr) {
        NormalizedLine normalizedLine = normalizeLine(lineStr);
        List<LineTemplate.Node> nodes = parseNormalizedLine(normalizedLine.getContent());
        return new LineTemplate(nodes, normalizedLine.getKind(), normalizedLine.getGroup());
    }

    private static NormalizedLine normalizeLine(String lineStr) {
        Matcher m = GROUP_PATTERN.matcher(lineStr);
        if (m.find()) {
            String idStr = m.group(1);
            Group group = Group.fromLowercaseIdentifier(idStr);
            String lineStrInner = RegexUtils.removeFirst(lineStr, GROUP_PATTERN);
            return normalizeLineInner(lineStrInner, group);
        } else {
            return normalizeLineInner(lineStr, null);
        }
    }

    private static NormalizedLine normalizeLineInner(String lineStr, Group group) {
        String s0 = RegexUtils.removeFirst(lineStr, EXCLUDED_PATTERN);
        if (!Objects.equals(s0, lineStr)) {
            return new NormalizedLine(s0, LineKind.EXCLUDED, group);
        }

        String s1 = RegexUtils.removeFirst(lineStr, HIDDEN_PATTERN);
        if (!Objects.equals(s1, lineStr)) {
            return new NormalizedLine(s1, LineKind.HIDDEN, group);
        }

        return new NormalizedLine(lineStr, LineKind.NORMAL, group);
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

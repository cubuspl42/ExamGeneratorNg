package pl.edu.pg.examgeneratorng;

import lombok.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

@Value
public class Placeholder {
    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([#!?])(\\d\\d)(/\\d\\d)?(-*)(\\\\*)");

    private PlaceholderKind kind;
    private int index;
    private int lineIndex; // 1-indexed
    private int width;
    private int height;

    String repr() {
        return symbol() + String.format("%02d", index) + suffix();
    }

    private String symbol() {
        switch (kind) {
            case CODE:
                return "#";
            case OUTPUT:
                return "!";
            case SECRET_OUTPUT:
                return "?";
        }
        throw new AssertionError();
    }

    private String suffix() {
        if (kind != PlaceholderKind.CODE) {
            return "/" + String.format("%02d", lineIndex);
        } else {
            return "";
        }
    }

    static Placeholder parse(String placeholderString) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(placeholderString);

        if(!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        String kindStr = matcher.group(1);
        String indexStr = matcher.group(2);
        String lineIndexStr = matcher.group(3);
        String minusesStr = matcher.group(4);
        String slashesStr = matcher.group(5);

        PlaceholderKind kind = charToPlaceholderKind(kindStr);
        int index = parseInt(indexStr);
        int lineIndex = lineIndexStr != null ? parseInt(lineIndexStr.substring(1)) : 0;
        int width = kindStr.length() +
                indexStr.length() +
                (lineIndexStr != null ? lineIndexStr.length() : 0) +
                minusesStr.length();
        int height = 1 + (slashesStr != null ? slashesStr.length() : 0);

        return new Placeholder(kind, index, lineIndex, width, height);
    }

    private static PlaceholderKind charToPlaceholderKind(String kindStr) {
        char c = kindStr.charAt(0);
        switch (c) {
            case '#':
                return PlaceholderKind.CODE;
            case '!':
                return PlaceholderKind.OUTPUT;
            case '?':
                return PlaceholderKind.SECRET_OUTPUT;
        }
        throw new IllegalArgumentException(kindStr);
    }
}

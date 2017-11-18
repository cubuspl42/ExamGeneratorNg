package pl.edu.pg.examgeneratorng;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static pl.edu.pg.examgeneratorng.MatrixPlaceholder.Kind.CODE;

@Value
class MatrixPlaceholder extends Placeholder {
    private int width;
    private int height;
    private int index;
    private int lineIndex;
    private Placeholder.Kind kind;

    static final Pattern MATRIX_PLACEHOLDER_PATTERN =
            Pattern.compile("^\\s*([#!?])(\\d\\d)(/\\d\\d)?(-*)(\\\\*)$\\s*");

    static MatrixPlaceholder parse(String text) {
        Matcher matcher = MATRIX_PLACEHOLDER_PATTERN.matcher(text);

        Preconditions.checkArgument(matcher.matches());

        String kind = matcher.group(1);
        String indexStr = matcher.group(2);
        String lineIndexStr = matcher.group(3);
        String minusesStr = matcher.group(4);
        String slashesStr = matcher.group(5);

        int index = indexStr == null ? 0 : parseInt(indexStr);
        int lineIndex = lineIndexStr != null ? parseInt(lineIndexStr.substring(1)) : 0;
        int width = kind.length() +
                (indexStr == null ? 0 : indexStr.length()) +
                (lineIndexStr != null ? lineIndexStr.length() : 0) +
                (minusesStr == null ? 0 : minusesStr.length());
        int height = 1 + (slashesStr != null ? slashesStr.length() : 0);

        return new MatrixPlaceholder(width, height, index, lineIndex, CODE.tryFindKindBy(kind));
    }

    static boolean isMatrixPlaceholder(String text) {

        Matcher matcher = MATRIX_PLACEHOLDER_PATTERN.matcher(text);
        return matcher.matches();
    }


    @Override
    String repr() {
        String suffix = kind != Kind.CODE ? "/" + String.format("%02d", lineIndex) : "";

        return kind.getSymbol() + String.format("%02d", index) + suffix;
    }


    @Getter
    @AllArgsConstructor
    public enum Kind implements Placeholder.Kind<Kind> {
        CODE("#"),
        OUTPUT("!"),
        SECRET_OUTPUT("?");

        private String symbol;

        public Kind tryFindKindBy(String symbol) {

            Optional<Kind> kind = Stream.of(values())
                    .filter(p -> p.hasSymbol(symbol))
                    .findFirst();

            Preconditions.checkArgument(kind.isPresent());

            return kind.get();
        }
    }
}

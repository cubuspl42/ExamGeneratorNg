package pl.edu.pg.examgeneratorng;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static pl.edu.pg.examgeneratorng.LinePlaceholder.Kind.GROUP;
import static pl.edu.pg.examgeneratorng.util.StringUtils.removeAllWhitespaces;

@Value
class LinePlaceholder extends Placeholder {
    private int width;
    private Placeholder.Kind kind;

    static final Pattern LINE_PLACEHOLDER_PATTERN = Pattern.compile("^\\s*\\$\\s*((?i)G|KIND)\\s*$");

    static LinePlaceholder parse(String text) {
        Preconditions.checkArgument(isLinePlaceholder(text));

        String kindSymbol = removeAllWhitespaces(text);
        int width = kindSymbol.length();

        return new LinePlaceholder(width, GROUP.tryFindKindBy(kindSymbol));
    }

    static boolean isLinePlaceholder(String text) {

        Matcher matcher = LINE_PLACEHOLDER_PATTERN.matcher(text);
        return matcher.matches();
    }


    @Override
    public String repr() {

        return kind.getSymbol();
    }


    @Getter
    @AllArgsConstructor
    public enum Kind implements Placeholder.Kind<Kind> {
        GROUP("$G"),
        KIND("$KIND");

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

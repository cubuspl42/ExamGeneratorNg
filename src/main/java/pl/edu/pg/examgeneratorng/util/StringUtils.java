package pl.edu.pg.examgeneratorng.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringUtils {
    public static String nCopiesOfChar(int n, char c) {
        return new String(new char[n]).replace("\0", "" + c);
    }

    public static List<String> splitByNewline(String str) {
        String parts[] = str.split("\\r?\\n", -1);
        return Arrays.asList(parts);
    }

    static List<String> stringToLines(String str) {
        List<String> parts = splitByNewline(str);
        if (Objects.equals(parts.get(parts.size() - 1), "")) {
            return parts.subList(0, parts.size() - 1);
        } else {
            return parts;
        }
    }

    public static String joinLines(List<String> lines) {
        return lines.stream()
                .map(line -> line + "\n")
                .collect(Collectors.joining());
    }


    public static String removeAllWhitespaces(String string) {

        return string.replaceAll("\\s", "");
    }
}

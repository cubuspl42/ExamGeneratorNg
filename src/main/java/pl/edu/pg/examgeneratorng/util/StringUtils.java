package pl.edu.pg.examgeneratorng.util;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
    public static String nCopiesOfChar(int n, char c) {
        return new String(new char[n]).replace("\0", "" + c);
    }

    public static List<String> splitByNewline(String str) {
        String lines[] = str.split("\\r?\\n");
        return Arrays.asList(lines);
    }
}

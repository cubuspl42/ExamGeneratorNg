package pl.edu.pg.examgeneratorng.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {
    public static List<MatchResult> findAll(String str, Pattern pattern) {
        List<MatchResult> result = new ArrayList<>();
        Matcher m = pattern.matcher(str);

        while (m.find()) {
            MatchResult matchResult = m.toMatchResult();
            result.add(matchResult);
        }

        return result;
    }
}

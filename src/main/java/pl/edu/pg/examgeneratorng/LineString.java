package pl.edu.pg.examgeneratorng;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.Value;

import java.util.List;

@Value
public class LineString {
    private List<String> lines;

    int longestLineLength() {
        return lines.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    static LineString fromSingleLine(String line) {
        Preconditions.checkArgument(!line.contains("\r"));
        Preconditions.checkArgument(!line.contains("\n"));
        return new LineString(ImmutableList.of(line));
    }
}

package pl.edu.pg.examgeneratorng;

import com.google.common.base.Preconditions;
import lombok.Value;

import java.util.regex.Pattern;

@Value
public class Group {
    public static final Group A = new Group(0);
    public static final Group B = new Group(1);

    private static final Pattern LOWERCASE_IDENTIFIER_PATTERN = Pattern.compile("[a-z]");

    private int index;

    public String getIdentifier() {
        char c = (char) ((int) 'A' + index);
        return "" + c;
    }

    static Group fromLowercaseIdentifier(String id) {
        Preconditions.checkArgument(id.length() == 1);
        Preconditions.checkArgument(LOWERCASE_IDENTIFIER_PATTERN.matcher(id).matches());
        int index = id.charAt(0) - 'a';
        return new Group(index);
    }
}

package pl.edu.pg.examgeneratorng;

import lombok.Value;

@Value
class Group {
    public static final Group A = new Group(0);

    private int index;

    String getIdentifier() {
        char c = (char) ((int) 'A' + index);
        return "" + c;
    }
}

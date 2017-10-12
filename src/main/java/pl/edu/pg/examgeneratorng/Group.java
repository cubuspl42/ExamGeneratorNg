package pl.edu.pg.examgeneratorng;

import lombok.Value;

@Value
class Group {
    private int index;

    String getIdentifier() {
        char c = (char) ((int) 'A' + index);
        return "" + c;
    }
}

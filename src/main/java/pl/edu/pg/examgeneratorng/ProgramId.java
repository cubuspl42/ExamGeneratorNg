package pl.edu.pg.examgeneratorng;

import lombok.Value;

@Value
public class ProgramId {
    private int id;

    public static ProgramId fromIndex(int index) {
        return new ProgramId(index + 1);
    }

    int toIndex() {
        return id - 1;
    }
}

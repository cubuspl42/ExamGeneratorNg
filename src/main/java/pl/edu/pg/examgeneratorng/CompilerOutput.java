package pl.edu.pg.examgeneratorng;

import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value
public class CompilerOutput {
    @NonNull
    List<String> diagnostics;
    @Nullable
    Program program;
}

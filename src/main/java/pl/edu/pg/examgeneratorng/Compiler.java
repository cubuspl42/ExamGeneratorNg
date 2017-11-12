package pl.edu.pg.examgeneratorng;

import java.util.List;

public interface Compiler {

    CompilerOutput compile(List<String> sourceCode);
}

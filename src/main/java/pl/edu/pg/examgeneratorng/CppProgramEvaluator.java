package pl.edu.pg.examgeneratorng;

public interface CppProgramEvaluator {

    ProgramOutput evaluate(Iterable<String> sourceCode);
}

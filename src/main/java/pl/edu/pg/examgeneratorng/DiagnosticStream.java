package pl.edu.pg.examgeneratorng;

@FunctionalInterface
public interface DiagnosticStream {
    void writeDiagnostic(Diagnostic diagnostic);
}

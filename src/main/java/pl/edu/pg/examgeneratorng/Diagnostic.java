package pl.edu.pg.examgeneratorng;

import lombok.Value;

@Value
public class Diagnostic {
    private DiagnosticKind kind;
    private String message;
}

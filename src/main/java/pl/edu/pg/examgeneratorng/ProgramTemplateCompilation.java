package pl.edu.pg.examgeneratorng;

import lombok.val;
import pl.edu.pg.examgeneratorng.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;
import static pl.edu.pg.examgeneratorng.util.StringUtils.joinLines;

final class ProgramTemplateCompilation {
    static Map<ProgramId, Map<Group, CompilerOutput>> compileProgramTemplates(
            Map<ProgramId, ProgramTemplate> programTemplateMap,
            ExamMetadata examMetadata,
            DiagnosticStream diagnosticStream
    ) {
        return programTemplateMap.entrySet().stream().collect(toMap(Entry::getKey, e -> {
            ProgramId programId = e.getKey();
            ProgramTemplate programTemplate = e.getValue();
            return compileProgramTemplate(programId, programTemplate, examMetadata, diagnosticStream);
        }));
    }

    private static Map<Group, CompilerOutput> compileProgramTemplate(
            ProgramId programId, ProgramTemplate programTemplate,
            ExamMetadata examMetadata,
            DiagnosticStream diagnosticStream
    ) {
        return IntStream.range(0, examMetadata.getGroupCount()).mapToObj(Group::new)
                .collect(toMap(identity(), group ->
                        compileProgramTemplate(programId, programTemplate, group, diagnosticStream)));
    }

    private static CompilerOutput compileProgramTemplate(
            ProgramId programId,
            ProgramTemplate programTemplate,
            Group group,
            DiagnosticStream diagnosticStream
    ) {
        LineString sourceCode = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, group);

        val compilerOutput = new GccCompiler().compile(sourceCode.getLines());

        List<String> diagnostics = compilerOutput.getDiagnostics();
        if (!diagnostics.isEmpty()) {
            boolean compilationSucceeded = compilerOutput.getProgram() != null;
            val diagnosticKind = compilationSucceeded ? DiagnosticKind.WARNING : DiagnosticKind.ERROR;
            val message = "Program " + programId.getId() + " (group " + group.getIdentifier() + "):\n" +
                    joinLines(diagnostics);
            diagnosticStream.writeDiagnostic(new Diagnostic(diagnosticKind, message));
        }

        return compilerOutput;
    }
}

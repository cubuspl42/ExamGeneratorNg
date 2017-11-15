package pl.edu.pg.examgeneratorng;

import lombok.val;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toMap;
import static pl.edu.pg.examgeneratorng.util.StringUtils.joinLines;

final class ProgramRunning {
    static Map<ProgramId, Map<Group, ProcessOutput>> runPrograms(
            Map<ProgramId, Map<Group, CompilerOutput>> compilerOutputMap,
            DiagnosticStream diagnosticStream
    ) {
        return compilerOutputMap.entrySet().stream().collect(toMap(Entry::getKey, e -> {
            ProgramId programId = e.getKey();
            Map<Group, CompilerOutput> compilerOutputInnerMap = e.getValue();
            return compilerOutputInnerMap.entrySet().stream().collect(toMap(Entry::getKey,
                    e1 -> {
                        val group = e1.getKey();
                        val compilerOutput = e1.getValue();
                        val programProcessOutput = compilerOutput.getProgram().execute();
                        int exitCode = programProcessOutput.getStatus();
                        if (exitCode != 0) {
                            val message = "Program " + programId.getId() + " (group " + group.getIdentifier() + ")" +
                                    " finished with non-zero exit code " + exitCode;
                            diagnosticStream.writeDiagnostic(new Diagnostic(DiagnosticKind.ERROR, message));
                            return null;
                        } else {
                            return programProcessOutput;
                        }
                    })
            );
        }));
    }
}

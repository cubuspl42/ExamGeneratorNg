package pl.edu.pg.examgeneratorng;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toMap;

final class ProgramRunning {
    static Map<ProgramId, Map<Group, ProgramOutput>> runPrograms(
            Map<ProgramId, Map<Group, CompilerOutput>> compilerOutputMap
    ) {
        return compilerOutputMap.entrySet().stream().collect(toMap(Entry::getKey, e -> {
            Map<Group, CompilerOutput> compilerOutputInnerMap = e.getValue();
            return compilerOutputInnerMap.entrySet().stream().collect(toMap(Entry::getKey,
                    e1 -> e1.getValue().getProgram().execute())
            );
        }));
    }
}

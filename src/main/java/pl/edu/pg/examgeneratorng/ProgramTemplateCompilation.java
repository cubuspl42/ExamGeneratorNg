package pl.edu.pg.examgeneratorng;

import lombok.Value;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;

final class ProgramTemplateCompilation {
    @Value
    private static class GroupCompilerOutput {
        private Group group;
        private CompilerOutput compilerOutput;
    }

    static Map<ProgramId, Map<Group, CompilerOutput>> compileProgramTemplates(
            Map<ProgramId, ProgramTemplate> programTemplateMap, ExamMetadata examMetadata
    ) {
        return programTemplateMap.entrySet().stream().collect(toMap(Entry::getKey, e -> {
            ProgramTemplate programTemplate = e.getValue();
            return compileProgramTemplate(programTemplate, examMetadata);
        }));
    }

    private static Map<Group, CompilerOutput> compileProgramTemplate(
            ProgramTemplate programTemplate, ExamMetadata examMetadata
    ) {
        return IntStream.range(0, examMetadata.getGroupCount()).mapToObj(Group::new)
                .map(group -> compileProgramTemplate(programTemplate, group))
                .collect(Collectors.toMap(GroupCompilerOutput::getGroup, GroupCompilerOutput::getCompilerOutput));
    }

    private static GroupCompilerOutput compileProgramTemplate(ProgramTemplate programTemplate, Group group) {
        LineString sourceCode = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, group);
        CompilerOutput compilerOutput = new GccCompiler().compile(sourceCode.getLines());
        return new GroupCompilerOutput(group, compilerOutput);
    }
}

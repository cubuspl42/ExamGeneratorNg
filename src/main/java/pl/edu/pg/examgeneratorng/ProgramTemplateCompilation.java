package pl.edu.pg.examgeneratorng;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;

final class ProgramTemplateCompilation {
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
                .collect(toMap(identity(), group -> compileProgramTemplate(programTemplate, group)));
    }

    private static CompilerOutput compileProgramTemplate(ProgramTemplate programTemplate, Group group) {
        LineString sourceCode = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, group);
        return new GccCompiler().compile(sourceCode.getLines());
    }
}

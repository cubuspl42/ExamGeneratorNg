package pl.edu.pg.examgeneratorng;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static pl.edu.pg.examgeneratorng.ExamTemplateLoading.findPlaceholders;
import static pl.edu.pg.examgeneratorng.ExamTemplateRealization.fillPlaceholders;
import static pl.edu.pg.examgeneratorng.ProgramRunning.runPrograms;
import static pl.edu.pg.examgeneratorng.ProgramTemplateCompilation.compileProgramTemplates;
import static pl.edu.pg.examgeneratorng.ProgramTemplateLoading.loadProgramTemplates;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;


public final class ExamGeneration {
    private static final int GROUPS_COUNT = 2;

    public static void generateAllExamVariants(Path workspacePath) throws Exception {
        ExamMetadata examMetadata = new ExamMetadata(GROUPS_COUNT);

        Map<ProgramId, ProgramTemplate> programTemplateMap = loadProgramTemplates(workspacePath);

        Map<ProgramId, Map<Group, CompilerOutput>> compilerOutputMap = compileProgramTemplates(
                programTemplateMap, examMetadata);

        Map<ProgramId, Map<Group, ProcessOutput>> processOutputMap = runPrograms(compilerOutputMap);

        Map<ProgramId, Map<Group, ProgramOutput>> programOutputMap = extractStandardOutputs(processOutputMap);

        generateAllExamVariants(workspacePath, programTemplateMap, programOutputMap);
    }

    private static Map<ProgramId, Map<Group, ProgramOutput>> extractStandardOutputs(
            Map<ProgramId, Map<Group, ProcessOutput>> programOutputMap
    ) {
        return programOutputMap.entrySet().stream().collect(toMap(Entry::getKey, e -> {
            Map<Group, ProcessOutput> groupProcessOutputMap = e.getValue();
            return groupProcessOutputMap.entrySet().stream().collect(toMap(Entry::getKey,
                    e1 -> new ProgramOutput(e1.getValue())
            ));
        }));
    }

    public static void generateAllExamVariants(
            Path workspacePath,
            Map<ProgramId, ProgramTemplate> programTemplateMap,
            Map<ProgramId, Map<Group, ProgramOutput>> programOutputMap
    ) throws Exception {
        IntStream.range(0, GROUPS_COUNT)
                .mapToObj(Group::new)
                .forEach(group -> {
                            Map<ProgramId, ProgramOutput> programOutputMapEx =
                                    extractGroupOutputs(programOutputMap, group);

                            Map<ProgramId, EvaluatedProgramTemplate> evaluatedProgramTemplateMap =
                                    programOutputMapEx.entrySet().stream().collect(toMap(
                                            Entry::getKey,
                                            e -> new EvaluatedProgramTemplate(
                                                    programTemplateMap.get(e.getKey()),
                                                    e.getValue()
                                            )));

                            generateExamVariantsForGroup(
                                    workspacePath,
                                    evaluatedProgramTemplateMap,
                                    group
                            );
                        }
                );
    }

    private static Map<ProgramId, ProgramOutput> extractGroupOutputs(
            Map<ProgramId, Map<Group, ProgramOutput>> programOutputMap, Group group
    ) {
        return programOutputMap.entrySet().stream().collect(toMap(Entry::getKey, e -> {
            Map<Group, ProgramOutput> groupProgramOutputMap = e.getValue();
            return groupProgramOutputMap.get(group);
        }));
    }

    private static void generateExamVariantsForGroup(
            Path workspacePath,
            Map<ProgramId, EvaluatedProgramTemplate> programTemplateMap,
            Group group
    ) {
        try {
            generateExamVariantForGroup(
                    workspacePath, programTemplateMap, group, ExamVariant.STUDENT);
            generateExamVariantForGroup(
                    workspacePath, programTemplateMap, group, ExamVariant.TEACHER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateExamVariantForGroup(
            Path workspacePath,
            Map<ProgramId, EvaluatedProgramTemplate> programTemplateMap,
            Group group,
            ExamVariant variant
    ) throws Exception {
        Exam exam = buildExamModel(programTemplateMap, group, variant);
        OdfTextDocument examTemplate = loadExamTemplate(workspacePath);
        OdfContentDom contentDom = examTemplate.getContentDom();
        List<PlaceholderRef> placeholderRefs = findPlaceholders(contentDom.getRootElement());
        fillPlaceholders(contentDom, placeholderRefs, exam, variant);
        examTemplate.save(workspacePath.resolve("exam_" + group.getIdentifier() + "_" + variant + ".odt").toFile());
    }

    private static Exam buildExamModel(
            Map<ProgramId, EvaluatedProgramTemplate> programTemplateMap, Group group, ExamVariant variant) {
        Map<ProgramId, ExamProgram> examProgramMap = programTemplateMap.entrySet().stream().collect(toMap(
                Entry::getKey,
                e -> buildExamProgram(e.getValue(), group, variant))
        );
        return Exam.builder()
                .group(group.getIdentifier())
                .examProgramMap(examProgramMap)
                .build();
    }

    private static ExamProgram buildExamProgram(
            EvaluatedProgramTemplate programTemplate, Group group, ExamVariant variant) {
        return ExamProgram.builder()
                .source(buildExamProgramSource(programTemplate.getProgramTemplate(), group, variant))
                .output(new LineString(programTemplate.getProgramOutput().getLines()))
                .build();
    }

    private static LineString buildExamProgramSource(
            ProgramTemplate programTemplate, Group group, ExamVariant variant) {
        if (variant == ExamVariant.STUDENT) {
            return realizeProgramTemplate(programTemplate, ProgramVariant.STUDENT, group);
        } else if (variant == ExamVariant.TEACHER) {
            return realizeProgramTemplate(programTemplate, ProgramVariant.TEACHER, group);
        }
        throw new AssertionError();
    }

    private static OdfTextDocument loadExamTemplate(Path workspacePath) {
        Path examTemplatePath = workspacePath.resolve("template.odt");
        try {
            return OdfTextDocument.loadDocument(examTemplatePath.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

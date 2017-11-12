package pl.edu.pg.examgeneratorng;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.edu.pg.examgeneratorng.ExamTemplateLoading.findPlaceholders;
import static pl.edu.pg.examgeneratorng.ExamTemplateRealization.fillPlaceholders;
import static pl.edu.pg.examgeneratorng.ProgramTemplateParsing.loadProgramTemplate;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;
import static pl.edu.pg.examgeneratorng.util.StringUtils.dumpLines;


public final class ExamGeneration {
    private static final int GROUPS_COUNT = 2;
    private static final int CODE_TEMPLATES_COUNT = 5;

    public static void generateAllExamVariants(Path workspacePath) throws Exception {
        List<ProgramTemplate> programTemplates = IntStream.range(0, CODE_TEMPLATES_COUNT)
                .mapToObj(i -> loadProgramTemplate(workspacePath.resolve(String.format("%02d.cpp", i + 1))))
                .collect(Collectors.toList());

        IntStream.range(0, GROUPS_COUNT).forEach(groupIndex ->
                generateExamVariantsForGroup(workspacePath, programTemplates, new Group(groupIndex)));
    }

    private static void generateExamVariantsForGroup(
            Path workspacePath, List<ProgramTemplate> programTemplates, Group group) {
        try {
            generateExamVariantForGroup(workspacePath, programTemplates, group, ExamVariant.STUDENT);
            generateExamVariantForGroup(workspacePath, programTemplates, group, ExamVariant.TEACHER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateExamVariantForGroup(
            Path workspacePath, List<ProgramTemplate> programTemplates, Group group, ExamVariant variant
    ) throws Exception {
        Exam exam = buildExamModel(programTemplates, group, variant);
        OdfTextDocument examTemplate = loadExamTemplate(workspacePath);
        OdfContentDom contentDom = examTemplate.getContentDom();
        List<PlaceholderRef> placeholderRefs = findPlaceholders(contentDom.getRootElement());
        fillPlaceholders(contentDom, placeholderRefs, exam, variant);
        examTemplate.save(workspacePath.resolve("exam_" + group.getIdentifier() + "_" + variant + ".odt").toFile());
    }

    private static Exam buildExamModel(
            List<ProgramTemplate> programTemplates, Group group, ExamVariant variant) {
        List<ExamProgram> examPrograms = programTemplates.stream()
                .map(p -> buildExamProgram(p, group, variant))
                .collect(Collectors.toList());
        return Exam.builder()
                .group(group.getIdentifier())
                .programs(examPrograms)
                .build();
    }

    private static ExamProgram buildExamProgram(
            ProgramTemplate programTemplate, Group group, ExamVariant variant) {
        return ExamProgram.builder()
                .source(buildExamProgramSource(programTemplate, group, variant))
                .output(runProgram(programTemplate, group))
                .build();
    }

    private static LineString runProgram(ProgramTemplate programTemplate, Group group) {

        LineString sourceCode = realizeProgramTemplate(programTemplate, ProgramVariant.COMPILER, group);
        String sourceCodeBuf = dumpLines(sourceCode.getLines());

        GppCppProgramEvaluator gppCppProgramEvaluator = new GppCppProgramEvaluator();
        ProgramOutput programOutput = gppCppProgramEvaluator.evaluate(sourceCodeBuf);

        return new LineString(programOutput.getLines());
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

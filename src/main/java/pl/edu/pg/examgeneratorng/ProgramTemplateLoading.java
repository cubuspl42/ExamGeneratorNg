package pl.edu.pg.examgeneratorng;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static pl.edu.pg.examgeneratorng.ProgramTemplateParsing.parseProgramTemplate;
import static pl.edu.pg.examgeneratorng.util.FileUtils.readWholeFile;

final class ProgramTemplateLoading {
    private static final int MAX_CODE_TEMPLATES_COUNT = 20;

    static Map<ProgramId, ProgramTemplate> loadProgramTemplates(Path workspacePath) {
        Map<ProgramId, ProgramTemplate> programTemplates = new HashMap<>();
        for (int i = 0; i < MAX_CODE_TEMPLATES_COUNT; ++i) {
            ProgramId programId = ProgramId.fromIndex(i);
            Path programTemplatePath = workspacePath.resolve(String.format("%02d.cpp", programId.getId()));
            try {
                ProgramTemplate programTemplate = loadProgramTemplate(programTemplatePath);
                programTemplates.put(programId, programTemplate);
            } catch (UncheckedIOException e) {
                break;
            }
        }
        return programTemplates;
    }

    private static ProgramTemplate loadProgramTemplate(Path codeTemplatePath) {
        String programTemplateStr = readWholeFile(codeTemplatePath);
        return parseProgramTemplate(programTemplateStr);
    }
}

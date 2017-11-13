package pl.edu.pg.examgeneratorng;

import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.util.StringUtils.splitByNewline;

final class ProgramTemplateParsing {
    static ProgramTemplate parseProgramTemplate(String programTemplateStr) {
        List<String> lines = splitByNewline(programTemplateStr);

        List<LineTemplate> lineTemplates = lines.stream()
                .map(LineTemplateParsing::parseLineTemplate)
                .collect(Collectors.toList());

        return new ProgramTemplate(lineTemplates);
    }
}

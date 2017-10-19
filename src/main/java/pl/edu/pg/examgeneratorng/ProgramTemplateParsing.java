package pl.edu.pg.examgeneratorng;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.LineTemplateParsing.parseLineTemplate;
import static pl.edu.pg.examgeneratorng.util.FileUtils.readWholeFile;
import static pl.edu.pg.examgeneratorng.util.StringUtils.splitByNewline;

final class ProgramTemplateParsing {
    static ProgramTemplate loadProgramTemplate(Path codeTemplatePath) {
        String programTemplateStr = readWholeFile(codeTemplatePath);
        return parseProgramTemplate(programTemplateStr);
    }

    static ProgramTemplate parseProgramTemplate(String programTemplateStr) {
        List<String> lines = splitByNewline(programTemplateStr);

        List<ProgramTemplate.LineNode> nodes = lines.stream()
                .map(lineStr -> new ProgramTemplate.LineNode(parseLineTemplate(lineStr)))
                .collect(Collectors.toList());

        return new ProgramTemplate(nodes);
    }
}

package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;
import pl.edu.pg.examgeneratorng.util.StringUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.util.FileUtils.readWholeFile;

final class ProgramTemplateParsing {
    static ProgramTemplate loadProgramTemplate(Path codeTemplatePath) {
        String programTemplateStr = readWholeFile(codeTemplatePath);
        return parseProgramTemplate(programTemplateStr);
    }

    static ProgramTemplate parseProgramTemplate(String programTemplateStr) {
        List<String> lines = StringUtils.splitByNewline(programTemplateStr);

        List<ProgramTemplate.LineNode> a = lines.stream()
                .map(lineStr -> new ProgramTemplate.LineNode(LineTemplateParsing.parseLineTemplate(lineStr)))
                .collect(Collectors.toList());

        ProgramTemplate programTemplate = new ProgramTemplate(ImmutableList.of(""), a);

        return programTemplate;
    }
}

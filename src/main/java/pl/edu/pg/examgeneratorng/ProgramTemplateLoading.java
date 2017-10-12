package pl.edu.pg.examgeneratorng;

import com.google.common.collect.ImmutableList;

import java.nio.file.Path;
import java.util.Collections;

final class ProgramTemplateLoading {
    static ProgramTemplate loadProgramTemplate(Path codeTemplatePath) {
        ProgramTemplate.Line line = new ProgramTemplate.Line("foo bar", Collections.emptySet());
        return new ProgramTemplate(ImmutableList.of(line)); // TODO
    }
}

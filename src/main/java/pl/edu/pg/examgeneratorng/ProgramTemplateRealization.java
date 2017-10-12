package pl.edu.pg.examgeneratorng;

final class ProgramTemplateRealization {
    static String realizeProgramTemplate(
            ProgramTemplate programTemplate, Group group, ProgramVariant variant) {
        return programTemplate.getContent() + " " + group.getIdentifier() + " " +  variant; // TODO
    }
}

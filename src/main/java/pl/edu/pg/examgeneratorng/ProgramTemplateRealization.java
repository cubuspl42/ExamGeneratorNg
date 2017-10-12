package pl.edu.pg.examgeneratorng;

final class ProgramTemplateRealization {
    static String realizeProgramTemplate(
            ProgramTemplate programTemplate, Group group, ProgramVariant variant) {
        return programTemplate.getLines().get(0) + " " + group.getIdentifier() + " " +  variant; // TODO
    }
}

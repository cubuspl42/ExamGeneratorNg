package pl.edu.pg.examgeneratorng;

import pl.edu.pg.examgeneratorng.LineTemplate.GapNode;
import pl.edu.pg.examgeneratorng.LineTemplate.LineKind;
import pl.edu.pg.examgeneratorng.LineTemplate.TextNode;

import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.util.StringUtils.nCopiesOfChar;

final class ProgramTemplateRealization {
    static LineString realizeProgramTemplate(
            ProgramTemplate programTemplate, Group group, ProgramVariant variant) {
        return new LineString(programTemplate.getLineTemplates().stream()
                .map(lineTemplate -> realizeLineTemplate(lineTemplate, group, variant))
                .collect(Collectors.toList()));
    }

    static String realizeLineTemplate(LineTemplate lineTemplate, Group group, ProgramVariant variant) {
        LineKind lineKind = lineTemplate.getKind();

        if (lineKind == LineKind.NORMAL) {
            return realizeLineTemplateNodes(lineTemplate, group, variant);
        } else if (lineKind == LineKind.EXCLUDED) {
            if (variant != ProgramVariant.COMPILER) {
                return realizeLineTemplateNodes(lineTemplate, group, variant);
            } else {
                return "";
            }
        } else if (lineKind == LineKind.HIDDEN) {
            if (variant == ProgramVariant.COMPILER) {
                return realizeLineTemplateNodes(lineTemplate, group, variant);
            } else {
                return "";
            }
        }
        throw new AssertionError();
    }

    private static String realizeLineTemplateNodes(LineTemplate lineTemplate, Group group, ProgramVariant variant) {
        return lineTemplate.getNodes().stream()
                .map(node -> realizeNode(node, group, variant))
                .collect(Collectors.joining());
    }

    private static String realizeNode(LineTemplate.Node node, Group group, ProgramVariant variant) {
        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            return textNode.getContent();
        } else if (node instanceof GapNode) {
            GapNode gapNode = (GapNode) node;
            if (variant == ProgramVariant.STUDENT) {
                int gapLength = gapNode.getContent().length();
                return nCopiesOfChar(gapLength, '_');
            } else {
                return gapNode.getContent();
            }
        }
        throw new AssertionError();
    }
}

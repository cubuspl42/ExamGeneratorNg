package pl.edu.pg.examgeneratorng;

import pl.edu.pg.examgeneratorng.LineTemplate.GapNode;
import pl.edu.pg.examgeneratorng.LineTemplate.LineKind;
import pl.edu.pg.examgeneratorng.LineTemplate.TextNode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.edu.pg.examgeneratorng.util.StringUtils.nCopiesOfChar;

public final class ProgramTemplateRealization {
    public static LineString realizeProgramTemplate(
            ProgramTemplate programTemplate, ProgramVariant variant, Group group) {
        List<String> lines = programTemplate.getLineTemplates().stream()
                .map(lineTemplate -> realizeLineTemplate(lineTemplate, variant, group))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());
        return new LineString(lines);
    }

    static Optional<String> realizeLineTemplate(LineTemplate lineTemplate, ProgramVariant variant, Group group) {
        Group lineTemplateGroup = lineTemplate.getGroup();
        if (lineTemplateGroup == null || lineTemplateGroup.equals(group)) {
            return realizeLineTemplateInner(lineTemplate, variant);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> realizeLineTemplateInner(LineTemplate lineTemplate, ProgramVariant variant) {
        LineKind lineKind = lineTemplate.getKind();

        if (lineKind == LineKind.NORMAL) {
            return Optional.of(realizeLineTemplateNodes(lineTemplate, variant));
        } else if (lineKind == LineKind.EXCLUDED) {
            if (variant != ProgramVariant.COMPILER) {
                return Optional.of(realizeLineTemplateNodes(lineTemplate, variant));
            } else {
                return Optional.empty();
            }
        } else if (lineKind == LineKind.HIDDEN) {
            if (variant == ProgramVariant.COMPILER) {
                return Optional.of(realizeLineTemplateNodes(lineTemplate, variant));
            } else {
                return Optional.empty();
            }
        }

        throw new AssertionError();
    }

    private static String realizeLineTemplateNodes(LineTemplate lineTemplate, ProgramVariant variant) {
        return lineTemplate.getNodes().stream()
                .map(node -> realizeNode(node, variant))
                .collect(Collectors.joining());
    }

    private static String realizeNode(LineTemplate.Node node, ProgramVariant variant) {
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

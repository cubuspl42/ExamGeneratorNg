package pl.edu.pg.examgeneratorng.ui.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Value;
import lombok.val;
import org.lucidfox.jpromises.Promise;
import org.lucidfox.jpromises.PromiseFactory;
import org.lucidfox.jpromises.core.ThrowingSupplier;
import org.lucidfox.jpromises.javafx.JavaFXPromiseFactory;
import pl.edu.pg.examgeneratorng.*;
import pl.edu.pg.examgeneratorng.ui.util.PromiseContext;
import pl.edu.pg.examgeneratorng.ui.util.PromiseUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.edu.pg.examgeneratorng.ExamGeneration.generateExamVariantsForGroup;
import static pl.edu.pg.examgeneratorng.ProgramRunning.runProgram;
import static pl.edu.pg.examgeneratorng.ProgramTemplateCompilation.compileProgramTemplate;
import static pl.edu.pg.examgeneratorng.ProgramTemplateLoading.loadProgramTemplates;
import static pl.edu.pg.examgeneratorng.ProgramTemplateRealization.realizeProgramTemplate;
import static pl.edu.pg.examgeneratorng.ui.util.PromiseUtils.promiseToMonadic;
import static pl.edu.pg.examgeneratorng.util.MapUtils.*;
import static pl.edu.pg.examgeneratorng.util.StringUtils.joinLines;

public class ProjectTask {


    @Value
    private static class Pipeline {
        private Promise<Map<ProgramId, ProgramPipeline>> programPipelineMap;
        private Map<Group, Promise<ExamPipeline>> examPipelineMap;
    }

    @Value
    private static class ProgramPipeline {
        private ProgramTemplate programTemplate;
        private Map<Group, GroupPipeline> groupPipelineMap;
    }

    @Value
    private static class GroupPipeline {
        private ProgramTemplate programTemplate;
        private Promise<CompilerOutput> compilerOutput;
        private Promise<ProcessOutput> processOutput;
    }

    @Value
    private static class ExamPipeline {
        private Promise<Void> documentsGenerated;
    }

    private final Path workspacePath;

    public enum State {
        RUNNING,
        FAILED,
        SUCCEEDED
    }

    private final PromiseFactory promiseFactory = new JavaFXPromiseFactory();

    private final PromiseContext promiseContext = new PromiseContext(promiseFactory);

    @Getter
    private final ObservableList<Diagnostic> diagnostics = FXCollections.observableArrayList();

    private final DiagnosticStream diagnosticStream = diagnostic ->
            Platform.runLater(() -> diagnostics.add(diagnostic));

    private final Pipeline pipeline;

    ProjectTask(Path workspacePath) {
        this.workspacePath = workspacePath;

        Pipeline pipeline = mainPipeline(workspacePath);

        this.pipeline = pipeline;
    }

    private Pipeline mainPipeline(Path workspacePath) {

        val programTemplatesPromise = supplyAsync(() -> loadProgramTemplates(workspacePath));
        Promise<Map<ProgramId, ProgramPipeline>> programPipeplinesPromise =
                programTemplatesPromise.then(programTemplates -> promiseFactory.resolve(
                        mapMapByEntry(programTemplates, entry -> {
                            val programId = entry.getKey();
                            val programTemplate = entry.getValue();
                            return programPipeline(programId, programTemplate);
                        }))
                );

        Map<Group, Promise<ExamPipeline>> examPipelines = mapToMap(ImmutableSet.of(Group.A, Group.B), group ->
                programPipeplinesPromise.then(programPipelines ->
                        promiseFactory.resolve(examPipeline(programPipelines, group))
                )
        );

        return new Pipeline(programPipeplinesPromise, examPipelines);
    }


    private ProgramPipeline programPipeline(ProgramId programId, ProgramTemplate programTemplate) {
        return new ProgramPipeline(
                programTemplate,
                ImmutableMap.of(
                        Group.A, groupPipeline(programId, programTemplate, Group.A),
                        Group.B, groupPipeline(programId, programTemplate, Group.B)
                )
        );
    }

    private GroupPipeline groupPipeline(ProgramId programId, ProgramTemplate programTemplate, Group group) {
        Promise<CompilerOutput> compilerOutputPromise = supplyAsync(() ->
                compileProgramTemplate(programId, programTemplate, group, diagnosticStream)
        );

        Promise<ProcessOutput> processOutput = compilerOutputPromise.then(compilerOutput -> supplyAsync(() ->
                runProgram(programId, compilerOutput, group, diagnosticStream)));

        return new GroupPipeline(
                programTemplate,
                compilerOutputPromise,
                processOutput
        );
    }


    private ExamPipeline examPipeline(Map<ProgramId, ProgramPipeline> programPipelineMap, Group group) {
        Map<ProgramId, Promise<ProcessOutput>> processOutputPromises = mapMapByValue(
                programPipelineMap,
                programPipeline -> programPipeline.groupPipelineMap.get(group).processOutput
        );

        Promise<Void> documentsGeneratedPromise =
                PromiseUtils.all(promiseFactory, processOutputPromises)
                        .then(processOutputs -> supplyAsync(() -> {
                            Map<ProgramId, EvaluatedProgramTemplate> evaluatedProgramTemplateMap = mapToMap(
                                    processOutputs.keySet(),
                                    programId -> new EvaluatedProgramTemplate(
                                            programPipelineMap.get(programId).programTemplate,
                                            new ProgramOutput(processOutputs.get(programId))
                                    )
                            );

                            generateExamVariantsForGroup(workspacePath, evaluatedProgramTemplateMap, group);

                            return null;
                        }));

        return new ExamPipeline(
                documentsGeneratedPromise
        );
    }

    private <T> Promise<T> supplyAsync(ThrowingSupplier<? extends T> supplier) {
        Promise<T> promise = promiseContext.supplyAsync(supplier);
        return promise;
    }

    public ObservableNumberValue getProgress() {
        return promiseContext.getProgress();
    }

    public ObservableValue<State> getState() {
        return new SimpleObjectProperty<>(State.RUNNING);
    }

    public ObservableValue<List<Program>> getPrograms() {
        return promiseToMonadic(pipeline.programPipelineMap)
                .map(programPipelineMap -> programPipelineMap.entrySet().stream().map(entry -> {
                            val programId = entry.getKey();
                            val programPipeline = entry.getValue();

                            return new Program(
                                    programId,
                                    mapMapByEntry(programPipeline.groupPipelineMap, entry1 -> {
                                        val group = entry1.getKey();
                                        val groupPipeline = entry1.getValue();

                                        val realizedProgramTemplate = realizeProgramTemplate(
                                                programPipeline.programTemplate, ProgramVariant.COMPILER, group);

                                        ObservableValue<String> stdout = promiseToMonadic(groupPipeline.processOutput)
                                                .map(y -> joinLines(y.getStandardOutput()))
                                                .orElse("");

                                        ObservableValue<String> stderr = promiseToMonadic(groupPipeline.processOutput)
                                                .map(y -> joinLines(y.getErrorOutput()))
                                                .orElse("");

                                        return new GroupProgram(
                                                programId,
                                                programPipeline.programTemplate,
                                                joinLines(realizedProgramTemplate.getLines()),
                                                stdout,
                                                stderr,
                                                new SimpleObjectProperty<>(programPipeline.toString())
                                        );
                                    })
                            );
                        }).collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }
}

package pl.edu.pg.examgeneratorng.ui.model;

import com.google.common.collect.ImmutableMap;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import lombok.Getter;
import lombok.Value;
import lombok.val;
import org.lucidfox.jpromises.Promise;
import org.lucidfox.jpromises.PromiseFactory;
import org.lucidfox.jpromises.core.ThrowingSupplier;
import org.lucidfox.jpromises.javafx.JavaFXPromiseFactory;
import pl.edu.pg.examgeneratorng.*;
import pl.edu.pg.examgeneratorng.ui.util.PromiseUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import static javafx.application.Platform.runLater;
import static pl.edu.pg.examgeneratorng.ExamGeneration.generateAllExamVariants;
import static pl.edu.pg.examgeneratorng.ExamGeneration.generateExamVariantsForGroup;
import static pl.edu.pg.examgeneratorng.ProgramRunning.runProgram;
import static pl.edu.pg.examgeneratorng.ProgramTemplateCompilation.compileProgramTemplate;
import static pl.edu.pg.examgeneratorng.ProgramTemplateLoading.loadProgramTemplates;
import static pl.edu.pg.examgeneratorng.ui.util.PromiseUtils.promiseToMonadic;
import static pl.edu.pg.examgeneratorng.util.MapUtils.*;

public class ProjectTask {

    @Value
    private static class Pipeline {
        private Map<ProgramId, ProgramPipeline> programPipelineMap;
        private Map<Group, ExamPipeline> examPipelineMap;
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

    private final ExecutorService executorService = new ForkJoinPool();

    @Getter
    private final ObservableList<Diagnostic> diagnostics = FXCollections.observableArrayList();

    private final Promise<Pipeline> pipeline;

    ProjectTask(Path workspacePath) {
        this.workspacePath = workspacePath;

        Promise<Pipeline> pipeline = supplyAsync(() -> {
            val programTemplates = loadProgramTemplates(workspacePath);

            Map<ProgramId, ProgramPipeline> programPipelineMap = mapMapByEntry(
                    programTemplates,
                    entry -> programPipeline(entry.getKey(), entry.getValue())
            );

            Map<Group, ExamPipeline> examPipelineMap = ImmutableMap.of(
                    Group.A, examPipeline(programPipelineMap, Group.A),
                    Group.B, examPipeline(programPipelineMap, Group.B)
            );

            return new Pipeline(
                    programPipelineMap,
                    examPipelineMap
            );
        });

        this.pipeline = pipeline;
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
                compileProgramTemplate(programId, programTemplate, group, diagnostics::add)
        );

        Promise<ProcessOutput> processOutput = compilerOutputPromise.then(compilerOutput -> supplyAsync(() ->
                runProgram(programId, compilerOutput, group, diagnostics::add)));

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
        return promiseFactory.supplyAsync(supplier, executorService::submit);
    }

    public ObservableDoubleValue getProgress() {
        return new SimpleDoubleProperty(0.5);
    }

    public ObservableValue<State> getState() {
        return new SimpleObjectProperty<>(State.RUNNING);
    }

    public ObservableValue<List<Program>> getPrograms() {
        return promiseToMonadic(pipeline)
                .map(pipeline ->
                pipeline.programPipelineMap.values().stream().map(w -> new Program(
                        new SimpleObjectProperty<String>(w.toString())
                )).collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }

    private Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 1);

                try {
                    generateAllExamVariants(workspacePath, diagnostic -> pushDiagnostic(diagnostic));
                } catch (Exception e) {
                    pushDiagnostic(new Diagnostic(DiagnosticKind.ERROR, e.toString()));
                    throw e;
                }

                pushDiagnostic(new Diagnostic(DiagnosticKind.INFO, "Exam generated successfully"));

                updateProgress(1, 1);

                return null;
            }
        };
        new Thread(task).start();
        return task;
    }

    private void pushDiagnostic(Diagnostic diagnostic) {
        runLater(() -> diagnostics.add(diagnostic));
    }

    private static State mapState(Worker.State state) {
        switch (state) {
            case READY:
                return State.RUNNING;
            case SCHEDULED:
                return State.RUNNING;
            case RUNNING:
                return State.RUNNING;
            case SUCCEEDED:
                return State.SUCCEEDED;
            case FAILED:
                return State.FAILED;
            default:
                throw new AssertionError();
        }
    }
}

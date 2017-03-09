package com.ceg.examContent;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.util.List;

public class TaskTypeSimpleOutput extends TaskType{

    public TaskTypeSimpleOutput() {
        super();
        super.setNoOfAnswers(1);
        name = "SimpleOutput";
        command = "Wyjście programu";
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku wykonania programu.";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) throws EmptyPartOfTaskException {
        answers.clear();
        try{
            answers.add(output.get(1));
        }
        catch (IndexOutOfBoundsException e) {
            answers.clear();
            System.out.println("Cannot generate answers. Empty output from .exe file.");
            throw new EmptyPartOfTaskException();
        }
         preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) throws EmptyPartOfTaskException {
        List<String> code = task.getText().getStandardCompilationCode();
        task.compiler.execute(code, "simple", output);
        if (getUpdateAnswers()) {
            task.getType().generateAnswers(task, output, task.getAnswers());
        }
        else {
            task.getType().preparePdfAnswers(task);
        }
    }
    
    @Override
    public void preparePdfAnswers(Task task){
        task.getPdfAnswers().clear();
        String label;
        if (0 < task.getLabels().size()) {
            label = task.getLabels().get(0);
        } else {
            label = "";
        }
        task.getPdfAnswers().add(label + " #placeForAnswer");
    }
}
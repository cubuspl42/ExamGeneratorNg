package com.ceg.examContent;

import java.util.List;

public class TaskTypeOwn extends TaskType{

    public TaskTypeOwn() {
        super();
        name = "OwnType";
        this.setUpdateAnswers(false);
        command = "Własne zadanie";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers){
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = task.getText().getStandardCompilationCode();
        task.compiler.execute(code, "owntype", output);
        task.getType().preparePdfAnswers(task);
    }

    @Override
    public void preparePdfAnswers(Task task){
        task.getPdfAnswers().clear();
        for(int i=0; i<task.getAnswers().size(); i++){
            String label;
            if (i < task.getLabels().size()) {
                label = task.getLabels().get(i);
            } else {
                label = "";
            }
            task.getPdfAnswers().add(label + " #placeForAnswer");
        }
    }
}
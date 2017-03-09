package com.ceg.examContent;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.util.ArrayList;
import java.util.List;

public class TaskTypeGaps extends TaskType{
   
    
    public TaskTypeGaps() {
        super();
        name="Gaps";
        command = "Uzupełnianie pól";
        this.setNoOfAnswers(0);
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        if(!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Brak");
            this.setNoOfAnswers(1);
        }
        else{
            
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) throws EmptyPartOfTaskException {
        List<String> code = new ArrayList<>(task.getText().getStandardCompilationCode());
        task.compiler.execute(code, "gaps", output);
        if (getUpdateAnswers()) {
            task.getType().generateAnswers(task, output, task.getAnswers());
        }
        else {
            task.getType().preparePdfAnswers(task);
        }
    }

    @Override
    public void preparePdfAnswers(Task task) {
        task.getPdfAnswers().clear();
        for(int i=0;i<this.getNoOfAnswers();i++){
            String label;
            if (i < task.getLabels().size()) {
                label = task.getLabels().get(i);
            } else {
                label = "";
            }
            task.getPdfAnswers().add(label + "#placeForAnswer");
        }
    }
}

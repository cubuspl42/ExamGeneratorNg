package com.ceg.examContent;

import com.ceg.compiler.CodeParser;
import com.ceg.exceptions.EmptyPartOfTaskException;

import java.util.List;

public class TaskTypeVarValue extends TaskType{

    public TaskTypeVarValue() {
        super();
        name = "VarValue";
        command = "Wartości zmiennych";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        answers.clear();
        setNoOfAnswers(Integer.MAX_VALUE-1);
        // jeśli nastąpił błąd kompilacji, wygeneruj odpowiedź: "Błąd"
        if(!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Błąd");
            this.setNoOfAnswers(1);
        }
        else{          
            try{
                int i=0;
                for(String line: output){
                    if(i<super.getNoOfAnswers()+1){
                        if(i>0)
                            answers.add(line);
                        i++;
                    }
                }
                this.setNoOfAnswers(i-1);
            }
            catch (IndexOutOfBoundsException e) {
                answers.clear();
                this.setNoOfAnswers(0);
                System.out.println("Cannot generate answers. Empty output from .exe file.");

            }
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) throws EmptyPartOfTaskException {
        List<String> code = task.getText().getStandardCompilationCode();
        CodeParser.addNewlineAfterEachCout(code);
        task.compiler.execute(code, "varvalue", output);
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
            task.getPdfAnswers().add(label + " #placeForAnswer");
        }
    }
}

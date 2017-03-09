package com.ceg.examContent;

import com.ceg.exceptions.EmptyPartOfTaskException;
import org.apache.commons.lang.SystemUtils;

import java.util.List;

public class TaskTypeLineNumbers extends TaskType{
    
    public TaskTypeLineNumbers() {
        super();
        name="LineNumbers";
        command = "Numery linii";
        lineNumbersVisibility = true;
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        answers.clear();
        setNoOfAnswers(Integer.MAX_VALUE-1);
        // jeśli nie nastąpił błąd kompilacji, nie dodaj jedną odpowiedź
        if(output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Brak błędów kompilacji.");
            this.setNoOfAnswers(1);
        }
        else{
            try{
                int answersCnt = 0;
                for(String line: output){
                    if(line.contains("error")){
                        String[] substr = line.split(":");
                        int lineNumber;
                        if(SystemUtils.IS_OS_WINDOWS) // windows uzywa dodatkowego znaku ':' po nazwie dysku
                            lineNumber = Integer.parseInt(substr[2]);
                        else
                            lineNumber = Integer.parseInt(substr[1]);
                        answers.add(String.valueOf(getPdfLineNumber(task.getText(), lineNumber)));
                        answersCnt++;
                    }               
                }
                this.setNoOfAnswers(answersCnt);
            }
           catch (IndexOutOfBoundsException e) {
                answers.clear();
                this.setNoOfAnswers(0);
               System.out.println("Cannot generate answers. Empty output from .exe file.");
            }
        }
        preparePdfAnswers(task);
    }
    
    int getPdfLineNumber(Text code, int lineNumber){
        int PdfLineCount=0;
        int StandardLineCount=0;
        boolean found = false;
        for(TextPart tp : code.getTextParts()){
            if(found) break;
            if(tp.getType().equals("[test]")) continue;
            int textPartLines = tp.lineCount();
            if(textPartLines + StandardLineCount >= lineNumber){
                if(tp.getType().equals("[hidden]")) return -1;         // błąd kompilacji w ukrytym kodzie???
                else {
                    PdfLineCount += (lineNumber - StandardLineCount);
                    found = true;
                }
            }
            if(!found && !tp.getType().equals("[hidden]")){
                PdfLineCount += textPartLines;
            }
            StandardLineCount += textPartLines;
        }
        if(lineNumber >= StandardLineCount){
            PdfLineCount = -1;                 // numer linii z blędem jest większy niż liczba linii kodu
        }
            
        return PdfLineCount;
    }

    @Override
    public void callExecute(Task task, List<String> output) throws EmptyPartOfTaskException {
        List<String> code = task.getText().getStandardCompilationCode();
        task.compiler.execute(code, "linenumbers", output);
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
package com.ceg.examContent;

import com.ceg.exceptions.EmptyPartOfTaskException;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa abstrakcyjna zawierająca dane typowe dla konkretnego typu zadania:
 * defaultContents - domyślna treść polecenia
 * name - nazwa zadania
 * params - parametry zadania
 * Oraz metody służące do generowania odpowiedzi.
 */
@XmlSeeAlso({
        TaskTypeComplexOutput.class,
        TaskTypeGaps.class,
        TaskTypeLineNumbers.class,
        TaskTypeReturnedValue.class,
        TaskTypeSimpleOutput.class,
        TaskTypeVarValue.class,
        TaskTypeOwn.class
})
abstract public class TaskType {
    String defaultContents;
    public String name;
    public String command;
    protected int noOfAnswers;
    private boolean updateAnswers;
    protected boolean lineNumbersVisibility;
    
    TaskType(){
        noOfAnswers = Integer.MAX_VALUE-1;
        updateAnswers = true;
        lineNumbersVisibility = false;
    }

    /**
     * Generuje odpowiedzi do zadania.
     * @param task Zadanie, dla którego ma zostać wygenerowana odpowiedź.
     * @param output Lista linii wygenerowanych przez kompilator dla kodu zawartego w zadaniu task.
     * @param answers Lista wyjściowa zawierająca odpowiedzi do zadania.
     */
    public abstract void generateAnswers(Task task, List<String> output, List<String> answers) throws EmptyPartOfTaskException;

    /**
     * Wykonuje kompilację kodu zawartego w zadaniu oraz wyznacza do niego odpowiedzi.
     * Dla każdej instrukcji wypisania generuje dodatkowy znak nowej linii.
     * @param task Zadanie które ma zostać wykonane.
     * @param output Lista linii w których zostanie zapisane wyjście kompilatora.
     */
    public abstract void callExecute(Task task, List<String> output) throws EmptyPartOfTaskException;

    /**
     * Wykonuje testową kompilację kodu zawartego w zadaniu oraz wyznacza do niego odpowiedzi.
     * Dla każdej instrukcji wypisania generuje dodatkowy znak nowej linii.
     * @param task Zadanie które ma zostać wykonane.
     * @param output Lista linii w których zostanie zapisane wyjście kompilatora.
     */
    public void callTestExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getText().getGUIText());
        task.compiler.execute(code, "test.cpp", output);
    }

    /**
     * Przygotowuje odpowiedzi, które znajdą się w pliku .pdf dla danego zadania.
     * @param task Zadanie, dla którego mają zostać przygotowane odpowiedzi.
     */
    public abstract void preparePdfAnswers(Task task);
    
    public String getDefaultContents() {
        return defaultContents;
    }
    public void setDefaultContents(String defaultContents) {
        this.defaultContents = defaultContents;
    }
    public int getNoOfAnswers() {
        return noOfAnswers;
    }
    public void setNoOfAnswers(int noOfAnswers) {
        this.noOfAnswers = noOfAnswers;
    }
    public void setUpdateAnswers(boolean updateAnswers) {
        this.updateAnswers = updateAnswers;
    } 
    public boolean getUpdateAnswers() {
        return this.updateAnswers;
    }    
    public void setLineNumbersVisibility(boolean lineNumbersVisibility) {
        this.lineNumbersVisibility = lineNumbersVisibility;
    } 
    public boolean getLineNumbersVisibility() {
        return this.lineNumbersVisibility;
    } 
}

package com.ceg.examContent;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Klasa Exam stanowi kontekst dla modelu danych aplikacji, dla każdego wywołania aplikacji istnieje jedna statyczna instancja.
 * Exam jest zbiorem obiektów klasy Task.
 */
@XmlRootElement
public class Exam extends Observable {
    private ArrayList<Task> tasks;
    private float executionTimetout = 4F;
    private int compilationProgress = -1;
    private List<String> outputList = new ArrayList<>();
    @XmlElement
    private ArrayList<String> names;
    private String filename;
    private static Exam instance;    
    private Content titleContent;
    private Content commentContent;

    /**
     * Indeks aktualnego zadania (wskazywanego przez zakładkę).
     */
    public int idx;

    /**
     * Aktualny indeks dodawanego zadania.
     */
    public int maxIdx;

    private Exam() {
    }
    public static Exam getInstance() {
        if (instance == null){
            synchronized(Exam.class){
                if(instance == null)
                    instance = new Exam();
            }
        }
        return instance;
    }
    public void init(){
        tasks = new ArrayList<>();
        names = new ArrayList<>();
        idx = 0;
        maxIdx = 0;
        filename = "";
        titleContent = new Content();
        commentContent = new Content();
    }
    
    public int compile() {
        List<String> output = new ArrayList<>();
        int result = 1;
        clearOutputList();
        for (Task i : tasks) {
            output.clear();
            try {
                i.getType().callExecute(i, output);
            } catch (EmptyPartOfTaskException e) {
                return -2;
            }
            i.setResult(String.join("\n", output));
            this.incCompilationProgress();
            if (output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
                if(output.get(1).contentEquals("Błąd wykonania.")){
                    addToOutputList("Zadanie " + (getCompilationProgress()+1) + " : " + "Błąd wykonania.\n");
                    result = -3;
                }
                else if(output.get(1).contentEquals("Upłynięcie limitu czasu wykonania.")){
                    addToOutputList("Zadanie " + (getCompilationProgress()+1) + " : " + "Przekroczenie limitu czasu.\n");
                    result = -4;
                }
                else{
                    addToOutputList("Zadanie " + (getCompilationProgress()+1) + " : " + output.get(0) + "\n");
                }
            }
            else {                
               if (!i.getType().name.equals("LineNumbers")){
                    output.remove(0);
                    output.stream().forEach((s) -> {
                        addToOutputList(s + "\n");
                    });
                   return -1;
               }
               else{                  
                   addToOutputList("Zadanie " + (getCompilationProgress()+1) + ": Błąd kompilacji w zadaniu \"Numery linii\".\n");
               }
            }            
        }
        this.incCompilationProgress();
        return result;
    }

    public float getExecutionTimetout() {
        return executionTimetout;
    }

    public void setExecutionTimetout(float executionTimetout) {
        this.executionTimetout = executionTimetout;
    }
        
    public List<Task> getTasks(){
        return tasks;
    }
    public void setTasks(List<Task> newTasks){
        tasks = (ArrayList<Task>) newTasks;
    }
    public List<String> getNames() {
        return names;
    }
    public void setNames(ArrayList<String> tabsNames) {
        this.names = tabsNames;
    }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    /**
     * Dodaje nowe zadanie do egzaminu.
     * @param t Zadanie które ma zostać dodane do egzaminu.
     */
    public void addTask(Task t){
        tasks.add(t);
        idx = tasks.size() - 1;
        maxIdx++;
        names.add("Zadanie " + maxIdx);
    }

    /**
     * Edytuje zadanie z egzaminu.
     * @param t Zadanie które jest edytowane.
     */
    public void editTask(Task t){
        int idx = tasks.indexOf(t);
        Task task = tasks.get(idx);
        task.setType(t.getType());
    }

    /**
     * Pobiera z egzaminu zadanie wskazywane przez aktualną zakładkę.
     * @return Odczytane zadanie.
     */
    public Task getCurrentTask(){
        return tasks.get(idx);
    }

    /**
     * Pobiera zadanie znajdujące się na podanej pozycji w egzaminie.
     * @param idx Liczba określająca numer zadania które ma zostać pobrane z egzaminu.
     * @return Odczytane zadanie.
     */
    public Task getTaskAtIndex(int idx){
        return tasks.get(idx);
    }

    /**
     * Usuwa zadanie znajdujące się na podanej pozycji w egzaminie.
     * @param idx Liczba określająca numer zadania które ma zostać usunięte.
     */
    public void deleteTaskAtIndex(int idx) {
        tasks.remove(idx);
        names.remove(idx);
    }
    
    public synchronized void incCompilationProgress(){
        compilationProgress++;
    }
    
    public synchronized int getCompilationProgress() {
        return compilationProgress;
    }
    
    public synchronized void clearCompilationProgress(){
        compilationProgress = -1;
    }
    
    public synchronized List<String> getOutputList() {
        return outputList;
    }
    
    private synchronized void addToOutputList(String str){
        outputList.add(str);
    }
    
    private synchronized void clearOutputList(){
        outputList.clear();
    }
    
    public void changeTasksOrder(int oldIndex, int newIndex) {
        Task task = tasks.get(oldIndex);
        tasks.remove(oldIndex);
        tasks.add(newIndex, task);

        String name = names.get(oldIndex);
        names.remove(oldIndex);
        names.add(newIndex, name);
    }

    /**
     * Zapisuje egzamin ze z góry zdefiniowaną nazwą.
     * Uruchamia okno wyboru pliku do zapisu.
     */
    public void save(String filename) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Exam.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, new File(filename));
        } catch (JAXBException e) {
            Alerts.examSavingErrorAlert();
            System.out.println("Cannot save exam. Error caused by: " + e.toString());
        }
    }

    /**
     * Wczytuje zawartość pliku do obiektu klasy Exam.
     * W przypadku niepowodzenia wyświetla odpowiedni alert.
     * @param filename Nazwa pliku który ma zostać odczytany.
     * @return Wartość określająca powodzenie operacji.
     */
    public boolean load(String filename) {
        try {
            JAXBContext context = JAXBContext.newInstance(Exam.class);
            Unmarshaller un = context.createUnmarshaller();
            Exam exam = (Exam)un.unmarshal(new File(filename));
            this.setTasks(exam.tasks);
            this.idx = exam.idx;
            this.maxIdx = exam.maxIdx;
            this.names = exam.names;
            this.filename = "";
        } catch (JAXBException e) {
            Alerts.wrongFileContentAlert();
            System.out.println("Cannot load exam. Error caused by: " + e.getCause().toString());
            return false;
        } catch (ClassCastException e) {
            Alerts.wrongFileContentAlert();
            System.out.println("Cannot load exam. Error caused by: " + e.toString());
            return false;
        }

        return true;
    }
    public Content getTitleContent() {
        return titleContent;
    }
    public Content getCommentContent() {
        return commentContent;
    }
    
    public void setTitleContent(Content content) {
        this.titleContent = content;
    }
    public void setCommentContent(Content content) {
        this.commentContent = content;
    }
}

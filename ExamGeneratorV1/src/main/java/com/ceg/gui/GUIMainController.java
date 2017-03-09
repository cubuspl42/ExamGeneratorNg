package com.ceg.gui;

import java.io.File;

import com.ceg.utils.Alerts;
import java.util.*;
import com.ceg.examContent.Text;
import com.ceg.utils.FileChooserCreator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import com.ceg.examContent.Content;
import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;

import javafx.scene.layout.Pane;
import com.ceg.exceptions.EmptyExamException;
import com.ceg.pdf.PDFSettings;
import static com.ceg.utils.ContentCssClass.*;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 * Klasa reprezentująca kontroler głównego okna programu.
 */
public class GUIMainController implements Initializable {

    @FXML
    MenuBar menu;
    @FXML
    BorderPane borderPane;
    @FXML
    StyleClassedTextArea text;
    @FXML
    CodeArea code;
    @FXML
    TabPane tabPane;
    @FXML
    TextArea result;
    @FXML
    StyleClassedTextArea answer;
    @FXML
    Button gapsMarkerBtn;
    @FXML
    Button labelBtn;
    @FXML
    Button answerBtn;
    @FXML
    MenuItem changeNameItem;
    @FXML
    MenuItem deleteTaskItem;
    @FXML
    MenuItem saveTaskItem;
    @FXML
    public MenuItem openPdfItem;
    @FXML
    MenuItem saveTaskAsItem;
    @FXML
    MenuItem saveExamItem;
    @FXML
    MenuItem saveExamAsItem;
    @FXML
    MenuItem taskEdition;
    @FXML
    MenuItem pdfContentWidth;
    @FXML
    HBox textOptions;
    @FXML
    CheckBox rememberCheckBox;
    @FXML
    CheckMenuItem lineNumbersCheckBox;
    
    public static Scene scene = null; 
    private static Stage stage = null;
    private static GUIMainController instance = null;
    private static Exam exam = null;
    public enum Status {
        ADD, DELETE, SWITCH, RENAME, DRAG
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status = Status.SWITCH;

    public static void setStageName (String str){
        stage.setTitle(str);
    }

    /**
     * Dokonuje inicjalizacji okna głównego, ustawia listenery na zmianę kodu oraz przełączanie zakładek.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        if(Exam.getInstance().idx == 0) {
            Exam.getInstance().init();
            exam = Exam.getInstance();
        }

        code.textProperty().addListener((observableValue, oldValue, newValue) -> {
            result.setText("");
            if (!rememberCheckBox.isSelected()) { 
                for (int i = 0; i < answer.getText().length(); i++) {
                    if (answer.getStyleOfChar(i).isEmpty() || answer.getStyleOfChar(i).toString().equals(EMPTY)) {
                        answer.deleteText(i, i+1);
                        i--;
                    }
                }
            }
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (status) {
                case RENAME:
                case DRAG:
                    break;
                case DELETE:
                    if(Integer.parseInt(oldValue.getId()) == 0) { // usuwana jest pierwsza pozycja
                        updateTabPaneTabIndexes();
                        updateWindow(0);
                    }
                    else {
                        if(newValue != null) {
                            updateTabPaneTabIndexes();
                            updateWindow(Integer.parseInt(newValue.getId()));
                        }
                    }
                    status = Status.SWITCH;
                    break;
                case ADD:
                    status = Status.SWITCH;
                case SWITCH:
                    if(oldValue != null) {
                        int id = Integer.parseInt(oldValue.getId());
                        saveText(id);
                        saveContent(id);
                        saveResult(id);
                        saveLabels(id);
                        exam.getTaskAtIndex(id).getType().setUpdateAnswers(true);
                        if (rememberCheckBox.isSelected()) {
                            exam.getTaskAtIndex(id).getType().setUpdateAnswers(false);
                            saveAnswers(id);
                        }
                        ((DraggableTab)oldValue).disableContextMenu();
                        saveTaskInfo(id);
                    }
                    ((DraggableTab)newValue).enableContextMenu();
                    updateWindow(Integer.parseInt(newValue.getId()));
                    break;
            }
        });

        rememberCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    if(new_val == true){
                        rememberCheckBox.tooltipProperty().set(new Tooltip("Włącza automatyczne generowanie odpowiedzi"));
                    }
                    else{
                        rememberCheckBox.tooltipProperty().set(new Tooltip("Wyłącza automatyczne generowanie odpowiedzi"));
                    }
            }
        });
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setWrapText(true);

        text.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.TAB) {
                String s = "    ";
                text.insertText(text.getCaretPosition(), s);
                e.consume();
            }
        });

        code.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.TAB) {
                String s = "    ";
                code.insertText(code.getCaretPosition(), s);
                e.consume();
            }
        });

        updateWindow(0);
    }

    /**
     * Wyświetla główne okno programu.
     * @throws IOException
     */
    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIMainController.class.getResource("/fxml/mainPage.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            scene = new Scene((Pane)loader.load(location.openStream()));
            boolean result;          
            result = scene.getStylesheets().add("/styles/Styles.css");
            if(!result){
                Alerts.stylesLoadingErrorAlert();
            }
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("CEG");
            stage.setResizable(false);
        }
        
        stage.show();
        stage.toFront();
        stage.setOnCloseRequest(e -> Platform.exit());
    }
    public static GUIMainController getInstance() {
        return instance;
    }

    /**
     * Wykonuje kod zawarty w polu CodeArea z uwzględnieniem markerów.
     * Rezultat zapisuje w odpowiednim obiekcie klasy Task i wyświetla w oknie programu.
     * Aktualnie wykonuje w sposób określony w Tasku, tj. dodaje entery do wyjścia.
     * @param actionEvent
     */
    public void execute(ActionEvent actionEvent) {
        result.clear();
        saveText(exam.idx);
        List<String> outcome = new ArrayList<>();
        exam.getCurrentTask().getType().setUpdateAnswers(true);
        
        if (rememberCheckBox.isSelected()) {
            exam.getCurrentTask().getType().setUpdateAnswers(false);
            saveAnswers(exam.idx);
        }

        try {
            exam.getCurrentTask().getType().callExecute(exam.getCurrentTask(), outcome);
        } catch (Exception e) {
            return;
        }
        for(String s : outcome) {
            result.appendText(s + "\n");
        }
        exam.getCurrentTask().setResult(result.getText());
        saveLabels(exam.idx);
        
        if (!rememberCheckBox.isSelected()) {
            updateAnswer(exam.getCurrentTask().getLabels(), exam.getCurrentTask().getAnswers(), exam.getCurrentTask().getType().getUpdateAnswers());           
        }
    }

    /**
     * Wykonuje kod zawarty w polu CodeArea bez uwzględnienia markerów.
     * Rezultat zapisuje w odpowiednim obiekcie klasy Task i wyświetla w oknie programu.
     * @param actionEvent
     */
    public void testExecute(ActionEvent actionEvent) {
        result.clear();
        saveText(exam.idx);
        List<String> outcome = new ArrayList<>();

        exam.getCurrentTask().getType().callTestExecute(exam.getCurrentTask(), outcome);
        for(String s : outcome) {
            result.appendText(s + "\n");
        }
        exam.getCurrentTask().setResult(result.getText());

    }
    
    public void changeToLabel(ActionEvent actionEvent) {
        IndexRange ir = answer.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {            
            answer.setStyleClass(i, i+1, BOLD.changeClass(answer.getStyleOfChar(i).toString()).getClassName());
        }
    }
    
    public void changeToAnswer(ActionEvent actionEvent) {
        IndexRange ir = answer.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {            
            answer.setStyleClass(i, i+1, UNDO.getClassName());
        }
    }

    /**
     * Otwiera okno generowania pliku .pdf na podstawie zadań zawartych w egzaminie.
     * @param actionEvent
     * @throws IOException
     */
    public void createPDF(ActionEvent actionEvent) throws IOException {
        try {
            if (exam.getTasks().isEmpty()) {
                throw new EmptyExamException();
            }
            saveTaskInfo(exam.idx);
            PdfSavingController.show();
        } catch (EmptyExamException ex) {
            Alerts.emptyExamAlert();
        }
    }
    
    public void openPDF(ActionEvent actionEvent) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File pdfFile = PDFSettings.getInstance().getPdfFile();
        if(pdfFile != null){  
            EventQueue EQ = new EventQueue();
            if(desktop.isSupported(Desktop.Action.OPEN)){
                 EventQueue.invokeLater(() -> {
                     try {
                         desktop.open(pdfFile);
                     } catch (IOException ex) {}
                 });
            }
        }
    }

    /**
     * Ustawia typ dla zaznaczonego kodu w polu CodeArea po naciśnięciu odpowiedniego przycisku.
     * @param actionEvent
     */
    public void testMarker(ActionEvent actionEvent) {
        changeStyle("test");
    }    
    public void hideMarker(ActionEvent actionEvent) {
        changeStyle("hidden");
    }    
    public void normalMarker(ActionEvent actionEvent) {
        changeStyle("normal");
    }  
    public void gapsMarker(ActionEvent actionEvent) {
        changeStyle("gap");
    }
    public void boldTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, BOLD.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    public void italicTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, ITALIC.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    public void underlineTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, UNDERLINE.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    public void undoTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        text.setStyleClass(ir.getStart(), ir.getEnd(), UNDO.getClassName());
    }
    public void monospaceTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, MONOSPACE.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    
    /**
     * Ustawia typ dla kodu zawartego w polu CodeArea.
     * @param className Nazwa typu do przypisania.
     */
    private void changeStyle(String className) {
        IndexRange ir = code.getSelection(); 
        int end = ir.getEnd();
        String c = code.getText();
        while (end < code.getLength()) {
            if (c.charAt(end) == '\n') {
                break;
            }
            end++;
        }
        
        code.setStyleClass(ir.getStart(), ir.getEnd(), className);
    }

    /**
     * Wyświetla okno dodawania nowego zadania.
     * @param event
     * @throws Exception
     */
    public void addTask(ActionEvent event) throws Exception {
        Text text = new Text();
        GUIManageTaskController.show("add");
    }

    /**
     * Wyświetla okno edycji zadania.
     * @param event
     * @throws Exception
     */
    public void editTask(ActionEvent event) throws Exception {
        GUIManageTaskController.show("edit");
        Task task = Exam.getInstance().getCurrentTask();
        Text tempText = new Text();
        tempText.extractText(code);
        task.setText(tempText);
        GUIManageTaskController.getInstance().editTask(task);
    }
    
    /**
     * Wyświetla okno edycji szerokości zadania w dokumencie
     * @param event
     * @throws Exception 
     */
    public void changePdfContentWidth(ActionEvent event) throws Exception {
        PdfWidthChangingController.show();
    }

    /**
     * Usuwa zadanie wskazywane przez aktywną zakładkę.
     * @param event
     * @throws Exception
     */
    public void deleteTask(ActionEvent event) throws Exception {
        if(Exam.getInstance().getTasks().isEmpty()) {
            showTask(false);
        }
        else {
            deleteCurrentTabPaneTab();
        }
    }

    /**
     * Zmienia nazwę aktualnie aktywnej zakładki.
     * @param event
     * @throws Exception
     */
    public void changeTaskName(ActionEvent event) throws Exception {
        status = Status.RENAME;
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Edycja nazwy zadania");
        textInputDialog.setHeaderText("Wpisz nową nazwę");
        Optional<String> result = textInputDialog.showAndWait();
        if(result.isPresent() && result.get().length() > 0) {
            DraggableTab tab = new DraggableTab(result.get());
            Exam.getInstance().getNames().set(exam.idx, result.get());
            tab.setId(Integer.toString(exam.idx));
            tabPane.getTabs().set(exam.idx, tab);
            tabPane.selectionModelProperty().get().select(exam.idx);
        }
        status = Status.SWITCH;
    }
    
    @FXML
    private void advancedOptionsClicked(MouseEvent event){
        try {
            AdvancedOptionsController.show();
        } catch (IOException ex) {
            Logger.getLogger(PdfSavingController.class.getName()).log(Level.SEVERE, null, ex); // TODO: obsluga wyjatku
        }
    }
    
    @FXML
    private void helpClicked(MouseEvent event){
        try {
            HelpController.show();
        } catch (IOException ex) {
            Logger.getLogger(PdfSavingController.class.getName()).log(Level.SEVERE, null, ex); // TODO: obsluga wyjatku
        }
    }
    /*
     * Ustawia widoczność elementów okna głównego.
     * @param visibility Określa żądaną widoczność elementów okna.
     */
    public void showTask(boolean visibility) {
        borderPane.setVisible(visibility);
        text.setVisible(visibility);
        code.setVisible(visibility);
        result.setVisible(visibility);
        labelBtn.setVisible(visibility);
        answerBtn.setVisible(visibility);
        rememberCheckBox.setVisible(visibility);
        changeNameItem.setVisible(visibility);
        deleteTaskItem.setVisible(visibility);
        saveTaskItem.setVisible(visibility);
        saveTaskAsItem.setVisible(visibility);
        saveExamItem.setVisible(visibility);
        saveExamAsItem.setVisible(visibility);
        taskEdition.setVisible(visibility);
        pdfContentWidth.setVisible(visibility);
        answer.setVisible(visibility);
        lineNumbersCheckBox.setVisible(visibility);
       
        if(visibility){
            if(exam.getTaskAtIndex(exam.idx).getType().name.contentEquals("Gaps")){
                gapsMarkerBtn.setVisible(visibility);
            }
            else{
                gapsMarkerBtn.setVisible(false);
            }
        }
        if (visibility) {
            if (exam.getTaskAtIndex(exam.idx).getType().name.contentEquals("OwnType")) {
                rememberCheckBox.setDisable(true);
            } 
            else {
                rememberCheckBox.setDisable(false);
            }
        }
    }

    /**
     * Odświeża zawartość okna.
     * @param idx Numer zadania które ma zostać wyświetlone.
     */
    public void updateWindow(int idx) {
        if(exam.getTasks().isEmpty()) {  // gdy egzamin nie zawiera żadnych zadań
            showTask(false); // ukryj elementy związane z Taskiem
        }
        else {         
            Task t = exam.getTaskAtIndex(idx);
            exam.idx = idx;

            showTask(true);
            updateText(t.getContent());
            updateCode(t.getText());
            updateResult(t.getResult());
            updateAnswer(t.getLabels(), t.getAnswers(), t.getType().getUpdateAnswers());
            lineNumbersCheckBox.setSelected(t.getType().getLineNumbersVisibility());
        }
    }

    /**
     * Aktualizuje tekst polecenia.
     * @param content Obiekt klasy Content zawierający informacje o tekście i stanie znaczników.
     */
    public void updateText(Content content) {
        content.creatStyleClassedTextAreaText(text);
    }

    /**
     * Aktualizuje tekst kodu.
     * @param text Obiekt klasy Text zawierający informacje o tekście i stanie znaczników.
     */
    public void updateCode(Text text) {
        text.createCodeAreaText(code);
    }

    /**
     * Aktualizuje tekst zwrócony przez kompilator.
     * @param text Tekst który ma zostać wyświetlony w polu wyjścia kompilatora.
     */
    public void updateResult(String text) {
        this.result.clear();
        this.result.setText(text);
    }
    
    /**
     * Aktualizuje tekst pola odpowiedzi.
     * @param labels
     * @param answers
     * @param updateAnswer
     */
    public void updateAnswer(List<String> labels, List<String> answers, boolean updateAnswer) {
        answer.clear();
        for (int i = 0; i < answers.size(); i++) {
            String label;
            if (i < labels.size()) {
                label = labels.get(i);
            } else {
                label = "";
            }
            int start = answer.getText().length();
            answer.appendText(label);
            answer.setStyleClass(start, answer.getText().length(), BOLD.getClassName());
            start = answer.getText().length();
            answer.appendText(answers.get(i) + "\n");
            answer.setStyleClass(start, answer.getText().length(), EMPTY.getClassName());
        }
        rememberCheckBox.setSelected(!updateAnswer);
    }

    /**
     * Dodaje nową zakładkę z zadaniem.
     */
    public void addNewTabPaneTab() {
        status = Status.ADD;
        DraggableTab newTab = new DraggableTab("Zadanie " + (exam.maxIdx));
        newTab.setId(Integer.toString(exam.idx));
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }

    /**
     * Usuwa aktywną zakładkę wraz z zawartym w niej zadaniem.
     */
    public void deleteCurrentTabPaneTab() {
        status = Status.DELETE;
        exam.deleteTaskAtIndex(exam.idx);
        tabPane.getTabs().remove(exam.idx);
    }

    /**
     * Aktualizuje indeksy zakładek w przypadku zmiany ich organizacji (np. usunięcia jednej z nich).
     */
    public void updateTabPaneTabIndexes() {
        for(int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setId(Integer.toString(i));
        }
    }

    /**
     * Zapisuje treść polecenia w odpowiednie pole obiektu reprezentującego dane zadanie.
     * @param idx Indeks zadania, dla którego ma zostać uaktualnione pole z poleceniem.
     */
    public void saveContent(int idx) {
        Task task = Exam.getInstance().getTaskAtIndex(idx);
        task.getContent().extractContent(text);
    }

    /**
     * Zapisuje wynik kompilacji w odpowiednie pole obiektu reprezentującego dane zadanie.
     * @param idx Indeks zadania, dla którego ma zostać uaktualnione pole z wynikiem kompilacji.
     */
    public void saveResult(int idx) {
        Exam.getInstance().getTaskAtIndex(idx).setResult(result.getText());
    }

    /**
     * Zapisuje informację czy do zadania mają zostać dodane numery linii oraz
     * kod wraz ze znacznikami w odpowiednie pole obiektu reprezentującego dane zadanie.
     * W przypadku zadania z lukami dodatkowo generuje odpowiedzi do zadania.
     * @param idx Indeks zadania, dla którego ma zostać uaktualnione pole z kodem.
     */
    public void saveText(int idx) {
        Task task = Exam.getInstance().getTaskAtIndex(idx);
        task.getText().extractText(code);
        if(task.getType().name.equals("Gaps")) {
            task.calculateGapsAnswers(task.getText().getTextParts());
            task.getType().setNoOfAnswers(task.getAnswers().size());
        }
        task.getType().setLineNumbersVisibility(lineNumbersCheckBox.isSelected());
    }

    /**
     * Zapisuje etykiety odpowiedzi
     */
    private void saveLabels(int id) {
        exam.getTaskAtIndex(id).getLabels().clear();
        String label = "";

        for (int i = 0; i < answer.getText().length(); i++) {
            if (answer.getText().charAt(i) == '\n') {
                exam.getTaskAtIndex(id).getLabels().add(label);
                label = "";
            }
            else if (answer.getStyleOfChar(i).toString().equals(BOLD.getClassList())) {
                label += answer.getText().charAt(i);
            }
        }
        if (!label.equals("")) {
            exam.getTaskAtIndex(id).getLabels().add(label);
        }
    }

    private void saveAnswers(int id) {
        exam.getTaskAtIndex(id).getAnswers().clear();
        String ans = "";
        int noOfAnswers = 0;

        for (int i = 0; i < answer.getText().length(); i++) {
            if (answer.getText().charAt(i) == '\n') {
                exam.getTaskAtIndex(id).getAnswers().add(ans);
                noOfAnswers++;
                ans = "";
            }
            else if (answer.getStyleOfChar(i).isEmpty() || answer.getStyleOfChar(i).toString().equals(EMPTY.getClassList())) {
                ans += answer.getText().charAt(i);
            }
        }
        if (!ans.equals("")) {
            exam.getTaskAtIndex(id).getAnswers().add(ans);
            noOfAnswers++;
        }
        exam.getTaskAtIndex(id).getType().setNoOfAnswers(noOfAnswers);
    }

    private void saveTaskInfo(int idx) {
        saveText(idx);
        saveContent(idx);
        saveResult(idx);
        saveLabels(idx);
        if (rememberCheckBox.isSelected()) {
            exam.getTaskAtIndex(idx).getType().setUpdateAnswers(false);
            saveAnswers(idx);
        } else {
            exam.getTaskAtIndex(idx).getType().setUpdateAnswers(true);
        }
    }

    /**
     * Zapisuje stan bieżącego zadania i generuje plik .xml z egzaminem.
     * Uruchamia okno wyboru pliku do zapisu w przypadku gdy w danej instancji programu nie był jeszcze wykonywany zapis.
     */
    public void saveExam() {
        saveTaskInfo(exam.idx);

        String filename = exam.getFilename();
        if(filename.isEmpty()) {
            File file = FileChooserCreator.getInstance().createSaveDialog(stage, FileChooserCreator.FileType.XML, "arkusz.xml");
            if (file == null) return;
            filename = file.getAbsolutePath();
            exam.setFilename(filename);
        }
        try {
            Exam.getInstance().save(filename);
        } catch (NullPointerException e) {
            Alerts.taskSavingErrorAlert();
            System.out.println("Cannot save task. Error caused by: " + e.toString());
        }
    }

    /**
     * Zapisuje stan bieżącego zadania i generuje plik .xml z egzaminem.
     * Uruchamia okno wyboru pliku do zapisu.
     */
    public void saveExamAs() {
        saveTaskInfo(exam.idx);

        File file = FileChooserCreator.getInstance().createSaveDialog(stage, FileChooserCreator.FileType.XML, "arkusz.xml");
        if (file == null) return;
        try {
            Exam.getInstance().save(file.getAbsolutePath());
        } catch (NullPointerException e) {
            Alerts.taskSavingErrorAlert();
            System.out.println("Cannot save task. Error caused by: " + e.toString());
        }
    }

    /**
     * Laduje egzamin do programu ze z góry określonego pliku.
     * Uruchamia okno wyboru pliku do odczytu.
     */
    public void loadExam() {
        try {
            File file = FileChooserCreator.getInstance().createLoadDialog(stage, FileChooserCreator.FileType.XML);
            if (file == null || !Exam.getInstance().load(file.getAbsolutePath())) {
                return;
            }
        } catch (Exception e) {
            System.out.println("Cannot load exam from .xml file. Error caused by: " + e.toString());
            return;
        }

        status = Status.DRAG;
        tabPane.getTabs().clear();
        status = Status.SWITCH;
        int size = Exam.getInstance().getTasks().size();

        for(int i = 0; i < size; i++) {
            DraggableTab newTab = new DraggableTab(Exam.getInstance().getNames().get(i));
            newTab.setId(Integer.toString(i));
            tabPane.getTabs().add(newTab);
        }

        tabPane.getSelectionModel().select(0);

        Text text = Exam.getInstance().getTaskAtIndex(0).getText();
        text.createCodeAreaText(code);

        Content content = Exam.getInstance().getTaskAtIndex(0).getContent();
        content.creatStyleClassedTextAreaText(this.text);
    }

    /**
     * Zapisuje stan bieżącego zadania w pliku (i egzaminie).
     * Uruchamia okno wyboru pliku do zapisu w przypadku gdy w danej instancji programu nie był jeszcze wykonywany zapis.
     */
    public void saveTask() throws Exception {
        saveTaskInfo(exam.idx);
        Task task = Exam.getInstance().getCurrentTask();
        String filename = task.getFilename();
        if(filename.isEmpty()) {
            File file = FileChooserCreator.getInstance().createSaveDialog(stage, FileChooserCreator.FileType.XML, Exam.getInstance().getNames().get(exam.idx).replace(" ", "") + ".xml");
            if (file == null) return;
            filename = file.getAbsolutePath();
            task.setFilename(filename);
        }
        try {
            task.save(filename);
        } catch (NullPointerException e) {
            Alerts.taskSavingErrorAlert();
            System.out.println("Cannot save task. Error caused by: " + e.toString());
        }
    }

    /**
     * Zapisuje stan bieżącego zadania w pliku (i egzaminie).
     * Uruchamia okno wyboru pliku do zapisu.
     */
    public void saveTaskAs() {
        saveTaskInfo(exam.idx);

        Task task = Exam.getInstance().getCurrentTask();

        File file = FileChooserCreator.getInstance().createSaveDialog(stage, FileChooserCreator.FileType.XML, Exam.getInstance().getNames().get(exam.idx).replace(" ", "") + ".xml");
        if (file == null) return;
        task.setFilename(file.getAbsolutePath());
        try {
            task.save(file.getAbsolutePath());
        } catch (NullPointerException e) {
            Alerts.taskSavingErrorAlert();
            System.out.println("Cannot save task. Error caused by: " + e.toString());
        }
    }

    /**
     * Odczytuje zadanie z pliku i otwiera je w nowej zakładce programu.
     * Uruchamia okno wyboru pliku do odczytu.
     * @param event
     */
    public void loadTask(ActionEvent event) {
        File file = FileChooserCreator.getInstance().createLoadDialog(stage, FileChooserCreator.FileType.XML);
        if (file == null) return;
        try {
            Task task = new Task();
            if (!task.load(file.getAbsolutePath())) {
                return;
            }
            Exam.getInstance().addTask(task);
            addNewTabPaneTab();
        } catch (NullPointerException e) {
            Alerts.taskLoadingErrorAlert();
            System.out.println("Cannot load task. Error caused by: " + e.toString());
        }
    }
}

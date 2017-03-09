package com.ceg.gui;

import com.ceg.examContent.*;
import com.ceg.utils.Alerts;
import com.ceg.utils.ContentCssClass;
import com.ceg.utils.FileChooserCreator;
import com.ceg.xml.TaskData;
import com.ceg.xml.TasksLoading;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.scene.control.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

/**
 * Klasa reprezentująca kontroler okna dodawania zadania.
 */
public class GUIManageTaskController implements Initializable {

    Content content = new Content();
    ArrayList<String> codeList = new ArrayList<>();
    TaskType type;
    @FXML
    TextArea text;
    @FXML
    CodeArea code;
    @FXML
    Button finish;
    @FXML
    Menu chooseType;
    @FXML
    MenuItem taskTypeSimpleOutput;
    @FXML
    MenuItem taskTypeReturnedValue;
    @FXML
    MenuItem taskTypeComplexOutput;
    @FXML
    MenuItem taskTypeGaps;
    @FXML
    MenuItem taskTypeVarValue;
    @FXML
    MenuItem taskTypeLineNumbers;
    @FXML
    MenuItem taskTypeOwn;
    @FXML
    BorderPane bPane;

    private static Stage stage = null;
    private static GUIManageTaskController manageTaskInstance = null;
    private static GUIMainController mainInstance = null;
    private FileChooser fileChooser;

    /**
     * Wyświetla okno dodawania zadania.
     * @throws IOException
     */
    public static synchronized void show(String action) throws IOException {
        if(stage == null) {
            URL location = GUIManageTaskController.class.getResource("/fxml/manageTask.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Scene scene = new Scene((Pane)loader.load(location.openStream()));

            stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
        }
        clearFields();
        if (action.equals("add")) {
            stage.setTitle("Dodaj nowe zadanie");
        }
        else if (action.equals("edit")) {
            stage.setTitle("Edycja zadania");
        }

        stage.show();
        stage.toFront();
        GUIManageTaskController.getInstance().finish.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manageTaskInstance = this;
        fileChooser = new FileChooser();
        mainInstance = GUIMainController.getInstance();
    }
    public static GUIManageTaskController getInstance() {
        return manageTaskInstance;
    }

    /**
     * Wpisuje do okna edycji zadania kod i polecenie z okna głównego.
     * @param task Zadanie pobrane z okna głównego.
     */
    public void editTask(Task task) {
        if (task.getType().name.equals("OwnType")) {
            text.setVisible(false);
            code.setPrefHeight(code.getPrefHeight()*3);
        }
        else updateText(task.getContent());
        task.getText().createCodeAreaText(code);
        type = task.getType();
        chooseType.setText(type.command);
        finish.setDisable(false);
    }

    /**
     * Aktualizuje tekst polecenia.
     * @param content Obiekt klasy Text zawierający informacje o tekście i stanie znaczników.
     */
    public void updateText(Content content) {
        if(!content.getContentParts().isEmpty()) {
            this.text.clear();
            int i = 0;
            String line;
            line = content.getContentParts().get(i).getText();
            while (i < content.getContentParts().size()) {
                this.text.appendText(line + "\n");
                i++;
                if (i >= content.getContentParts().size()) break;
                line = content.getContentParts().get(i).getText();
            }
        }
        else {
            Alerts.updateTextErrorAlert();
        }
    }

    /**
     * Wpisuje do okna treść polecenia dla wybranego typu zadania.
     * @param index Numer wybranego zadania.
     */
    public void addType(int index) {
        TaskData tasks = TasksLoading.loadFromXml();

        content.setContentParts(new ArrayList<>());
        text.clear();
        try {
            content.getContentParts().add(new ContentPart(ContentCssClass.EMPTY, tasks.getTaskData().get(index).getText()));
            //contentList.add(tasks.getTaskData().get(index).getText());
            text.appendText(tasks.getTaskData().get(index).getText());
        } catch (NullPointerException e) {
            Alerts.addTypeErrorAlert();
            System.out.println("Cannot load command text. Error caused by: " + e.toString());
        }

        finish.setDisable(false);
    }

    /**
     * Ustawia typ wybranego zadania, zmienia nagłówek okna dodawania zadania na odpowiadający wybranemu typowi.
     */
    public void addTypeSimpleOutput() {
        resetFields();
        chooseType.setText(taskTypeSimpleOutput.getText());
        GUIMainController.setStageName("CEG - " + taskTypeSimpleOutput.getText());
        addType(0);
        type = new TaskTypeSimpleOutput();
    }
    public void addTypeReturnedValue() {
        resetFields();
        chooseType.setText(taskTypeReturnedValue.getText());
        GUIMainController.setStageName("CEG - " + taskTypeReturnedValue.getText());
        addType(1);
        type = new TaskTypeReturnedValue();
    }
    public void addTypeComplexOutput() {
        resetFields();
        chooseType.setText(taskTypeComplexOutput.getText());
        GUIMainController.setStageName("CEG - " + taskTypeComplexOutput.getText());
        addType(2);
        type = new TaskTypeComplexOutput();
    }
    public void addTypeGaps() {
        resetFields();
        chooseType.setText(taskTypeGaps.getText());
        GUIMainController.setStageName("CEG - " + taskTypeGaps.getText());
        addType(3);
        type = new TaskTypeGaps();
    }
    public void addTypeVarValue() {
        resetFields();
        chooseType.setText(taskTypeVarValue.getText());
        GUIMainController.setStageName("CEG - " + taskTypeVarValue.getText());
        addType(4);
        type = new TaskTypeVarValue();
    }
    public void addTypeLineNumbers() {
        resetFields();
        chooseType.setText(taskTypeLineNumbers.getText());
        GUIMainController.setStageName("CEG - " + taskTypeLineNumbers.getText());
        addType(5);
        type = new TaskTypeLineNumbers();
    }

    public void addOwnType() {
        chooseType.setText(taskTypeOwn.getText());
        GUIMainController.setStageName("CEG - " + taskTypeOwn.getText());
        text.setVisible(false);
        code.setPrefHeight(bPane.computeAreaInScreen());
        addType(6);
        type = new TaskTypeOwn();
    }

    /**
     * Kończy tworzenie zadania w GUI. Tworzy nowy obiekt zadania, uzupełnia jego dane i zapisuje w egzaminie.
     * Dodaje nową zakładkę z zadaniem i przełącza sie na nią.
     * @param event
     * @throws Exception
     */
    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task(type);
        t.setContent(content);
        t.getText().extractText(code);

        if (stage.getTitle().equals("Dodaj nowe zadanie")) {
            Exam.getInstance().addTask(t);
            GUIMainController.getInstance().addNewTabPaneTab();
        }
        else if (stage.getTitle().equals("Edycja zadania")) {
            Exam.getInstance().editTask(Exam.getInstance().getCurrentTask());
            Task task = Exam.getInstance().getCurrentTask();
            task.setType(type);
            task.setContent(content);
            task.getText().extractText(code);
            if (!task.getContent().getContentParts().isEmpty()) {
                mainInstance.updateText(task.getContent());
            }
            mainInstance.updateCode(task.getText());
            mainInstance.showTask(true);
        }

        stage.hide();
        chooseType.setText("Typ zadania");
    }

    /**
     * Przywraca pole polecenia po dodawaniu własnego typu zadania
     */
    public static void resetFields() {
        manageTaskInstance.text.setVisible(true);
        manageTaskInstance.code.setPrefHeight(200.0);
    }

    /**
     * Czyści pola znajdujące się w oknie dodawania zadania.
     */
    public static void clearFields() {
        manageTaskInstance.text.clear();
        manageTaskInstance.code.clear();
        manageTaskInstance.codeList = new ArrayList<>();
        manageTaskInstance.content = new Content();
        manageTaskInstance.finish.setDisable(true);
        resetFields();
    }

    /**
     * Przerywa edycję i zamyka okno dodawania zadania.
     * @param event
     * @throws Exception
     */
    public void cancelEdition(ActionEvent event) throws Exception {
        GUIMainController.setStageName("CEG");
        stage.hide();
    }

    /**
     * Wyświetla okno wyboru pliku i ładuje go po zatwierdzeniu.
     */
    public void selectCodeFile() throws IOException {

        File file = FileChooserCreator.getInstance().createLoadDialog(stage, FileChooserCreator.FileType.CODE);
        if (file == null) return;
        try {
            loadFile(file);
        } catch(NullPointerException e) {
            Alerts.codeLoadingErrorAlert();
            System.out.println("Cannot load code from file. Error caused by: " + e.toString());
        }
    }

    /**
     * Laduje plik wybrany w oknie wyboru wpisując jego zawartość w polu CodeArea.
     * @param file
     * @throws IOException
     */
    public void loadFile(File file) throws IOException {
        Scanner s = new Scanner(file);
        codeList.clear();
        while(s.hasNext()) {
            codeList.add(s.nextLine());
        }
        s.close();
        code.clear();
        for (String i : codeList) {
            code.appendText(i + "\n");
        }
    }
}

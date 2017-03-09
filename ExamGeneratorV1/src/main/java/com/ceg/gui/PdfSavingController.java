package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.pdf.PDFSettings;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.ceg.utils.Alerts;
import com.ceg.utils.FileChooserCreator;
import javafx.beans.value.ChangeListener;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Klasa reprezentująca kontroler okna do generowania pliku .pdf.
 */
public class PdfSavingController implements Initializable {

    @FXML
    ChoiceBox testType;
    @FXML
    ChoiceBox dateDay;
    @FXML
    ChoiceBox dateMonth;
    @FXML
    ChoiceBox dateYear;

    private final List<String> testTypeList = Arrays.asList("student", "nauczyciel");
    private final List<String> monthList = new ArrayList<>();
    private final List<String> yearList = new ArrayList<>();
    
    private final ObservableList<String> daysList = FXCollections.observableList(new ArrayList<String>());
    
    public static Stage stage;
    private PDFSettings pdfSettings;
    private String initialDirectory;

    /**
     * Dokonuje inicjalizacji głównego okna generacji pliku .pdf.
     * Ustawia domyślne wartości, listenery.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        pdfSettings = PDFSettings.getInstance();
        
        for (Integer i = 1; i <= 12; i++) {
            if (i < 10) {
                monthList.add('0' + i.toString());
            }
            else {
                monthList.add(i.toString());
            }
        }
        
        testType.setItems(FXCollections.observableList(testTypeList));
        testType.setValue(pdfSettings.getTestType());
        
        dateMonth.setItems(FXCollections.observableList(monthList));
        Integer month = pdfSettings.getMonth();
        if (month < 10)
            dateMonth.setValue('0' + month.toString());
        else
            dateMonth.setValue(month.toString());
        
        Integer year = pdfSettings.getYear();
        for (Integer i  = 2008; i <= year+10; i++) {
            yearList.add(i.toString());
        }
        dateYear.setItems(FXCollections.observableList(yearList));
        dateYear.setValue(year.toString());
        
        YearMonth yM = YearMonth.of(year, month);
        Integer days = yM.lengthOfMonth();
        for (Integer i = 1; i <= days; i++) {
            daysList.add(i.toString());
        }
        dateDay.setItems(daysList);
        dateDay.setValue(pdfSettings.getDay().toString());

        // todo zamienić listenery na zapis przy nacisnięciu przycisku 'zapisz' (aktualizacja ustawień przy każdej zmianie jest zbyteczna)
        dateMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                daysList.clear();
                PDFSettings.getInstance().setMonth(newValue.intValue() + 1);
                YearMonth yM = YearMonth.of(PDFSettings.getInstance().getYear(), PDFSettings.getInstance().getMonth());
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(PDFSettings.getInstance().getDay().toString());
            }
        });
        
        dateDay.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() != -1)
                    PDFSettings.getInstance().setDay(Integer.parseInt(daysList.get(newValue.intValue())));
            }           
        });
        
        dateYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                PDFSettings.getInstance().setYear(Integer.parseInt(yearList.get(newValue.intValue())));
                
                daysList.clear();
                
                YearMonth yM = YearMonth.of(PDFSettings.getInstance().getYear(), PDFSettings.getInstance().getMonth());
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(PDFSettings.getInstance().getDay().toString());
            }           
        });
    }

    /**
     * Wyświetla okno generowania pliku .pdf.
     * @throws IOException
     */
    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIMainController.class.getResource("/fxml/pdfSaving.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Zapisz plik");
            stage.setResizable(false);
        }
        
        stage.show();
        stage.toFront();
    }

    /**
     * Zapisuje egzamin w formie pliku .pdf.
     * @param event
     * @throws IOException
     */

    public void saveFile(ActionEvent event) throws IOException {
        File file = FileChooserCreator.getInstance().createSaveDialog(stage, FileChooserCreator.FileType.PDF, "egzamin.pdf");
        if (file == null) return;
        try {
            if(validateExam()) {
                FileChooserCreator.getInstance().setInitialDirectory(file.getParent());
                PDFSettings.getInstance().setTestType(testType.getValue().toString());
                PDFSettings.getInstance().setPdfFile(file);
                PDFSettings.getInstance().formatDate();
                GUIExamCompilationController.show();
                stage.hide();
            }
            else {
                Alerts.emptyPartOfTaskAlert();
                stage.hide();
            }
        } catch (NullPointerException e) {
            Alerts.examSavingErrorAlert();
            System.out.println("Cannot save exam. Error caused by: " + e.toString());
        }
    }

    /**
     * Kończy proces generacji pliku .pdf (przed wciśnięciem przycisku 'Zapisz plik').
     * @param event
     */
    public void cancel(ActionEvent event) {
        stage.hide();
    }

    /**
     * Otwiera okno ustawień zaawansowanych.
     * @param event
     */
    public void advancedOptions(ActionEvent event){
        try {
            AdvancedOptionsController.show();
        } catch (IOException ex) {
            Alerts.advancedOptionsErrorAlert();
            System.out.println("Cannot open window: advanced options. Error caused by: " + ex.toString());
        }
    }

    public boolean validateExam() {
        Exam exam = Exam.getInstance();
        for(Task task : exam.getTasks()) {
            if(task.getText().getTextParts().get(0).getText().length() == 0 ||
                    task.getContent().getContentParts().get(0).getText().length() == 0) {
                return false;
            }
        }
        return true;
    }
}
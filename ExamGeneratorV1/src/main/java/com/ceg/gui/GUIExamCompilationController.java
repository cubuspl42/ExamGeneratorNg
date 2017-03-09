package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
import com.ceg.utils.Alerts;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Cursor;

/**
 * FXML Controller class
 *
 * @author Martyna
 */
public class GUIExamCompilationController implements Initializable {
    @FXML
    ProgressBar progressBar;
    
    @FXML
    Label taskNumberLabel;
    
    @FXML
    TextArea compilationDetails;
    
    @FXML
    Button saveButton;

    public static Stage appStage;
    private static boolean cancelled = false;
    private int taskIdx;
    private static GUIExamCompilationController compilationInstance = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        saveButton.setDisable(true);
        compilationInstance = this;
    }
    
    public static GUIExamCompilationController getInstance() {
        return compilationInstance;
    }
    
    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = GUIExamCompilationController.class.getResource("/fxml/examCompilation.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setScene(scene);
            appStage.setTitle("Kompilacja egzaminu");
            appStage.setResizable(false);
            appStage.setOnHidden(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    compilationInstance.clearControls();
                    GUIMainController.getInstance().updateWindow(Exam.getInstance().idx);
                }
            });
        } 
       
        compilationInstance.progressUpdate();
     //   detailsUpdate();
        compilationInstance.examCompile();         
        appStage.show();
        appStage.toFront();
    }
    
    public void save(ActionEvent event) throws IOException {
        appStage.hide();
        PdfSavingController.stage.hide();
        GUIMainController.scene.setCursor(Cursor.WAIT);
        PdfLoadingController.show();
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    PDFSettings.getInstance().pdfGenerate(PdfSavingController.stage);
                } catch (IOException e) {}
                return null ;
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                GUIMainController.scene.setCursor(Cursor.DEFAULT);
                PdfLoadingController.getInstance().stage.hide();
            }
        });
        Thread th = new Thread(task);
        th.start();
    }
    
    public void cancel(ActionEvent event) {
        cancelAndQuit();
    }
    
    private void cancelAndQuit(){
        setCancelled(true);
        appStage.hide();
        PdfSavingController.stage.hide();
    }
    
     public synchronized static boolean getCancelled() {
        return cancelled;
    }

    public synchronized static void setCancelled(boolean cancelled) {
        GUIExamCompilationController.cancelled = cancelled;
    }
    
    private void progressUpdate() {
        final Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {
                Exam exam = Exam.getInstance();
                int progress= -1;
                int prevProgress = -2;       // progress i prevProgress na poczatku nie mogą być takie same, bo na starcie
                                             // nie zaktualizuje się progressBar
                while ((progress = exam.getCompilationProgress()) + 1 < exam.getTasks().size()) {   
                    if (getCancelled()) {
                        return null;
                    }
                    if(prevProgress == progress) continue;
                    updateProgress(progress + 1, exam.getTasks().size());
                    updateTitle("Trwa kompilacja zadania " + (progress + 2) + " z " + exam.getTasks().size() + "...");
                    updateMessage(String.join("", exam.getOutputList()));
                    prevProgress = progress;
                }
                updateProgress(1, 1);
                updateMessage(String.join("", exam.getOutputList())+"Kompilacja zakończona pomyślnie.");
                return null;
            }
        };
        Exam.getInstance().clearCompilationProgress();
        clearControls();
        taskNumberLabel.textProperty().bind(task.titleProperty());
        compilationDetails.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    // czyści kontrolki związane  z wyświetlaniem postępu kompilacji
    private void clearControls(){
        setCancelled(false);
        taskNumberLabel.textProperty().unbind();
        compilationDetails.textProperty().unbind();
        progressBar.progressProperty().unbind();
        taskNumberLabel.textProperty().set("");
        compilationDetails.textProperty().set("");
        progressBar.progressProperty().set(0);
    }
    
    private void examCompile() {
        final Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {
                saveButton.setDisable(true);
                int retVal = Exam.getInstance().compile();
                boolean compilationOk = ((retVal == 1) || (retVal == -3) || (retVal == -4));
                saveButton.setDisable(!compilationOk);
                if(!compilationOk){
                    setCancelled(true);
                    Platform.runLater(new Runnable(){
                         @Override public void run() {
                             if (retVal != -2) {
                                 Alerts.compileErrorAlert();
                             } else {
                                 Alerts.generatingAnswersErrorAlert();
                             }
                            cancelAndQuit();
                         }
                    });
                }
                else{
                    Platform.runLater(new Runnable(){
                         @Override public void run() {
                             if (retVal == -3) {
                                 Alerts.executionErrorAlert();
                             } else if (retVal == -4){
                                 Alerts.executionTimetoutErrorAlert();
                             }
                         }
                    });
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}

package com.ceg.gui;

import com.ceg.examContent.Exam;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Martyna
 */
public class PdfWidthChangingController implements Initializable {
    public static Stage appStage;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 100;
    private static PdfWidthChangingController instance = null;
    
    @FXML
    Slider widthSlider;
    @FXML
    Label contentLabel;
    @FXML
    Label codeLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        widthSlider.setMin(MIN_VALUE);
        widthSlider.setMax(MAX_VALUE);
        widthSlider.setValue((int)(Exam.getInstance().getCurrentTask().getContent().getPdfWidthPercentage() * 100));
        widthSlider.setMajorTickUnit((MAX_VALUE - MIN_VALUE)/2);
        widthSlider.setBlockIncrement(10);
        
        widthSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            contentLabel.setText(new_val.intValue() + "%");
            codeLabel.setText((MAX_VALUE - new_val.intValue()) + "%");
        });
    }
    
    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = PdfWidthChangingController.class.getResource("/fxml/pdfWidthChanging.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setScene(scene);
            appStage.setTitle("Szerokość zadania");
            appStage.setResizable(false);
        }
        PdfWidthChangingController.getInstance().setCurrentTaskWidth();
        appStage.show();
        appStage.toFront();
    }
    
    public void accept(ActionEvent event) {
        Exam.getInstance().getCurrentTask().getContent().setPdfWidthPercentage((float) (widthSlider.getValue() / 100.0f));
        appStage.hide();
    }
    
    public void cancel(ActionEvent event) {
        appStage.hide();
    }
    
    public void setCurrentTaskWidth() {
        widthSlider.setValue((int)(Exam.getInstance().getCurrentTask().getContent().getPdfWidthPercentage() * 100));
    }
    
    public static PdfWidthChangingController getInstance() {
        return instance;
    }
}
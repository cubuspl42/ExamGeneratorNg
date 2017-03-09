/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.gui;

import static com.ceg.gui.PdfSavingController.stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Marta
 */
public class PdfLoadingController implements Initializable{

    @FXML
    ProgressIndicator pi;
    
    private static PdfLoadingController instance = null;
    public static Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        pi.setProgress(-1.0F);
    }
    
    public static PdfLoadingController getInstance(){
        return instance;
    }
    
     public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIMainController.class.getResource("/fxml/pdfLoading.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Czekaj...");
            stage.setResizable(false);
        }
        
        stage.show();
        stage.toFront();
    }
}

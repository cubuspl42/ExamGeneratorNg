package com.ceg.gui;

import static com.ceg.gui.AdvancedOptionsController.appStage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marta
 */
public class HelpController implements Initializable {

    private static HelpController helpInstance = null;
    private static Stage stage = null;
    
    public static HelpController getInstance() {
        return helpInstance;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         helpInstance = this;
    }    
    
     public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = HelpController.class.getResource("/fxml/help.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Scene scene = new Scene((Pane)loader.load(location.openStream()));

            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Poradnik użytkownika");
            stage.setResizable(false);
        }
        stage.show();
        stage.toFront();
    }
}

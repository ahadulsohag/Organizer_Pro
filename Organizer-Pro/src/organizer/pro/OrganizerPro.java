/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package organizer.pro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ahadu
 */
public class OrganizerPro extends Application {
    
@Override
public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(
            getClass().getResource("/organizer/pro/FXMLDocument.fxml"));

    Scene scene = new Scene(root);
    stage.setMinWidth(340);
    stage.setMinHeight(520);
    stage.setTitle("Organizer Pro");
    stage.setScene(scene);
    stage.show();   // only once
}


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

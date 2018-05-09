package de.oszimt.vysioneer;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
   
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    			
        URL res = getClass().getResource("/ClientMain.fxml");
    	//URL res = getClass().getResource("/ClientControl.fxml");
    	//URL res = getClass().getResource("/ServerControl.fxml");
    	//URL res = getClass().getResource("/ClientControl.fxml");
        if(res == null){
        	System.out.println("test");
        	return;
        }
        Parent root = FXMLLoader.load(res);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

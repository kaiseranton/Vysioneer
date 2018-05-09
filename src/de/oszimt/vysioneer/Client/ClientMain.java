package de.oszimt.vysioneer.Client;

import java.net.InetAddress;
import java.net.URL;

import de.oszimt.vysioneer.Client.Config.ConfigManager;
import de.oszimt.vysioneer.Client.SocketClient.SocketClientManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {
    
	public static ConfigManager cm;
	public static String name;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    			
    	
    	
		
		try {
            InetAddress addr = java.net.InetAddress.getLocalHost();    
            name = addr.getHostName();
		} catch (Exception e){
			
		}
    	
    	cm = new ConfigManager();
    	
        URL res = getClass().getResource("/ClientMain.fxml");
    	//URL res = getClass().getResource("/ClientControl.fxml");
    
        if(res == null){
        	System.out.println("test");
        	return;
        }
        Parent root = FXMLLoader.load(res);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
     
    }


}


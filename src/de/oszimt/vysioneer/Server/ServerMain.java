package de.oszimt.vysioneer.Server;

import java.net.URL;

import de.oszimt.vysioneer.Server.Client.ClientManager;
import de.oszimt.vysioneer.Server.Controller.ServerMainController;
import de.oszimt.vysioneer.Server.Security.PinManager;
import de.oszimt.vysioneer.Server.SocketServer.SocketServerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain  extends Application {
    
	public static PinManager pm;
	public static ServerMainController smc;
	public static SocketServerManager ssm;
	public static ClientManager cm;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    			
        URL res = getClass().getResource("/ServerMain.fxml");
    	//URL res = getClass().getResource("/ServerControl.fxml");
        if(res == null){
        	System.out.println("test");
        	return;
        }
        FXMLLoader loader = new FXMLLoader(res);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
        cm = new ClientManager();
        smc = (ServerMainController) loader.getController();
        pm = new PinManager();
       
        
        ssm = new SocketServerManager(8080);
        ssm.start();
        
        System.out.println("TESTEST");
        
    }
    
}

package de.oszimt.vysioneer.Server.Controller;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

import de.oszimt.vysioneer.Client.ClientMain;
import de.oszimt.vysioneer.Server.ServerMain;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class ServerMainController implements Initializable {
	@FXML    
	Text TextServerAddress;
	
	@FXML
	Text TextServerPin;
	
	@FXML
	ListView<String> ServerListe;
		
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		String name = "ERROR";
		try {
            InetAddress addr = java.net.InetAddress.getLocalHost();    
            name = addr.getHostName();
		} catch (Exception e){
			
		}
		TextServerAddress.setText(name);
	}   
	public void SetPin(String pin){
		TextServerPin.setText(pin);
	}
	public void RefreshClientList(){
		ObservableList<String> items = FXCollections.observableArrayList();
		for(int i = 0; i < ServerMain.cm.GetClients().size(); i++){
			items.add(ServerMain.cm.GetClients().get(i).GetName());
		}
        ServerListe.setItems(items);
	}
}
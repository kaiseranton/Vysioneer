package de.oszimt.vysioneer.Client.Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import de.oszimt.vysioneer.Client.ClientMain;
import de.oszimt.vysioneer.Client.Config.ConfigServer;
import de.oszimt.vysioneer.Client.SocketClient.SocketClientManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

	public class ClientMainController implements Initializable {
	
	@FXML
	ListView<String> ServerListe;
		
	@FXML
	Text UserName;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//ServerListe.getLi

		UserName.setText(ClientMain.name);
		LoadServerList();
		System.out.println("Index: "+ ServerListe.getSelectionModel().getSelectedIndex());
		
		/*
		 * 
		 * BUG!
		 * Wenn 3 Server in der Liste sind und alle Server1 - Server3 heißen
		 * und Server 2 gelöscht wird und ein neuer dazu kommt wird der neue auch Server3 heißen
		 * 
		 * BUG!
		 * Change und Add Server EmptyField Check!
		 * 
		 * 
		 */
	}    
	
	public void LoadServerList(){
		ObservableList<String> items = FXCollections.observableArrayList();
        for(int i = 0; i < ClientMain.cm.GetServerList().size(); i++){
        	items.add(ClientMain.cm.GetServerList().get(i).GetName());
        }
		
        ServerListe.setItems(items);
	}
	@FXML
	private void ConnectToServer(final ActionEvent event){
		//ClientMain.cm.DeleteServer(ServerListe.getSelectionModel().getSelectedIndex());
		//LoadServerList();
		
		if(ServerListe.getSelectionModel().getSelectedIndex() == -1){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnung!");
			alert.setHeaderText("Bitte wähle zuerst einen Server aus!");
			alert.showAndWait();
			return;
		}
		
		ConfigServer server = ClientMain.cm.GetServerList().get(ServerListe.getSelectionModel().getSelectedIndex());
		
		SocketClientManager man = new SocketClientManager(server.GetIP(),8080);
		while(!man.IsDone()){
			try {Thread.sleep(10);} catch (Exception e) {} //fix freeze error
		}
		if(man.WasSuccessfully()){
			TextInputDialog dialog = new TextInputDialog("2");
			dialog.setTitle("Pin-Abfrage");
			dialog.setHeaderText("Bitte gebe hier den PIN von den Lehrer-PC ein.");
			dialog.setContentText("PIN:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				try {
				    SocketClientManager authman = new SocketClientManager(server.GetIP(), 8080,Integer.parseInt(result.get()));
				    while(!authman.IsDone()){
				    	try {Thread.sleep(10);} catch (Exception e) {} //fix freeze error
				    }
				    if(authman.IsPinCorrect()){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Meldung");
						alert.setHeaderText("Verbindung erfolgreich hergestellt!");
						alert.show();
						
						// control gui öffnen und checkserver erstellen
						
				    } else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Warnung!");
						alert.setHeaderText("Falscher Pin!");
						alert.show();
				    }
				} catch (Exception e){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Warnung!");
					alert.setHeaderText("Falscher Pin!");
					alert.show();
				}

			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnung!");
			alert.setHeaderText("Es konnte keine Verbindung hergestellt werden!");
			alert.showAndWait();
		}
		
	}

	@FXML
	private void DeleteServer(final ActionEvent event){
		if(ServerListe.getSelectionModel().getSelectedIndex() == -1){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnung!");
			alert.setHeaderText("Bitte wähle zuerst einen Server aus!");
			alert.showAndWait();
			return;
		}
		
		ClientMain.cm.DeleteServer(ServerListe.getSelectionModel().getSelectedIndex());
		LoadServerList();
	}
	@FXML
	private void ChangeServer(final ActionEvent event){
		if(ServerListe.getSelectionModel().getSelectedIndex() == -1){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warnung!");
			alert.setHeaderText("Bitte wähle zuerst einen Server aus!");
			alert.showAndWait();
			return;
		}
		
	     Dialog<Pair<String, String>> dialog = new Dialog<>();
	     dialog.setTitle("Change Server");
	     ButtonType loginButtonType = new ButtonType("Übernehmen", ButtonData.OK_DONE);
	     dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
	     GridPane grid = new GridPane();
	     grid.setHgap(10);
	     grid.setVgap(10);
	     grid.setPadding(new Insets(20, 150, 10, 10));
	     ConfigServer serverc = ClientMain.cm.GetServerList().get(ServerListe.getSelectionModel().getSelectedIndex());
	     TextField anzeigename = new TextField();
	     anzeigename.setPromptText(serverc.GetName());
	     TextField serveradresse = new TextField();
	     serveradresse.setPromptText(serverc.GetIP());
	     anzeigename.setText(serverc.GetName());
	     serveradresse.setText(serverc.GetIP());
	     grid.add(new Label("Anzeigename:"), 0, 0);
	     grid.add(anzeigename, 1, 0);
	     grid.add(new Label("Server-Adresse:"), 0, 1);
	     grid.add(serveradresse, 1, 1);
	
	  
	     dialog.getDialogPane().setContent(grid);

	     Platform.runLater(() -> serveradresse.requestFocus());

	     dialog.setResultConverter(dialogButton -> {
	         if (dialogButton == loginButtonType) {
	             return new Pair<>(anzeigename.getText(), serveradresse.getText());
	         }
	         return null;
	     });

	     Optional<Pair<String, String>> result = dialog.showAndWait();

	     result.ifPresent(returnValues -> {
	 		ClientMain.cm.DeleteServer(ServerListe.getSelectionModel().getSelectedIndex());

	    	 
	         System.out.println("Name=" + returnValues.getKey() + ", IP=" + returnValues.getValue());
	         ConfigServer server;
	         if(returnValues.getKey().length() == 0){
	        	 server = new ConfigServer("Server" + (ClientMain.cm.GetServerList().size() + 1), returnValues.getValue());
	         } else {
	        	 server = new ConfigServer(returnValues.getKey(), returnValues.getValue());
	         }
	         ClientMain.cm.AddServer(server);
	         LoadServerList();
	     });
	}
	
	@FXML
	private void AddNewServer(final ActionEvent event){
	     Dialog<Pair<String, String>> dialog = new Dialog<>();
	     dialog.setTitle("Add new Server");
	     ButtonType loginButtonType = new ButtonType("Hinzufügen", ButtonData.OK_DONE);
	     dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
	     GridPane grid = new GridPane();
	     grid.setHgap(10);
	     grid.setVgap(10);
	     grid.setPadding(new Insets(20, 150, 10, 10));
	     TextField anzeigename = new TextField();
	     anzeigename.setPromptText("(optional)");
	     TextField serveradresse = new TextField();
	     serveradresse.setPromptText("192.168.1.1");
	     grid.add(new Label("Anzeigename:"), 0, 0);
	     grid.add(anzeigename, 1, 0);
	     grid.add(new Label("Server-Adresse:"), 0, 1);
	     grid.add(serveradresse, 1, 1);
	     Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
	     loginButton.setDisable(true);
	     
	     serveradresse.textProperty().addListener((observable, oldValue, newValue) -> {
	         loginButton.setDisable(newValue.trim().isEmpty());
	     });
	  
	     dialog.getDialogPane().setContent(grid);

	     Platform.runLater(() -> serveradresse.requestFocus());

	     dialog.setResultConverter(dialogButton -> {
	         if (dialogButton == loginButtonType) {
	             return new Pair<>(anzeigename.getText(), serveradresse.getText());
	         }
	         return null;
	     });

	     Optional<Pair<String, String>> result = dialog.showAndWait();

	     result.ifPresent(returnValues -> {
	         System.out.println("Name=" + returnValues.getKey() + ", IP=" + returnValues.getValue());
	         ConfigServer server;
	         if(returnValues.getKey().length() == 0){
	        	 server = new ConfigServer("Server" + (ClientMain.cm.GetServerList().size() + 1), returnValues.getValue());
	         } else {
	        	 server = new ConfigServer(returnValues.getKey(), returnValues.getValue());
	         }
	         ClientMain.cm.AddServer(server);
	         LoadServerList();
	     });
	}
}

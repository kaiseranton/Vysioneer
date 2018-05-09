package de.oszimt.vysioneer.Server.Client;

import java.util.ArrayList;

public class ClientManager {
	
	private ArrayList<Client> clients;
	
	
	public ClientManager(){
		clients = new ArrayList<Client>();
	}
	
	public ArrayList<Client> GetClients(){
		return clients;
	}
	public void AddClient(Client c){
		clients.add(c);
	}
	
}

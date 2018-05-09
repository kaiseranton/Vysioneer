package de.oszimt.vysioneer.Server.Client;

public class Client {

	private String ip;
	private String name;
	
	public Client(String n,String i){
		name = n;
		ip = i;
	}
	public String GetName(){
		return name;
	}
	public String GetIP(){
		return ip;
	}
	
}

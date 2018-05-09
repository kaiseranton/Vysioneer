package de.oszimt.vysioneer.Client.Config;

public class ConfigServer {
	
	String name;
	String ip;
	
	public ConfigServer(String n, String i){
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

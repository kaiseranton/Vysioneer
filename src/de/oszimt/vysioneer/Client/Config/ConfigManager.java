package de.oszimt.vysioneer.Client.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigManager {
	
	JsonParser jsonparser;
	JsonObject configobject;
	File ConfigFile;
	
	public ConfigManager(){
		
		jsonparser = new JsonParser();
		configobject = new JsonObject();
		
		String folderlocation = System.getProperty("user.dir") + "\\.Vysioneer";
		File VysioneerFolder = new File(folderlocation);
		if(!VysioneerFolder.exists())
			VysioneerFolder.mkdir();
		
		String configfilelocation = folderlocation + "\\config.vysioneer";
		ConfigFile = new File(configfilelocation);
		if(!ConfigFile.exists()){
			try {
				ConfigFile.createNewFile();	
			} catch (Exception e){
				//useless
			}
		} else {
			String content = null;
			try {
				content = new String(Files.readAllBytes(Paths.get(ConfigFile.getPath())));
				configobject = jsonparser.parse(content).getAsJsonObject();
			} catch (Exception e){
				
			}
			
			// config einlesen
		}
		if(!configobject.has("serverlist")){
			configobject.add("serverlist", new JsonArray());
			Save();
		}
		
		/*configobject.addProperty("Name", "Vysioneer");
		JsonArray serverlist = new JsonArray();
		JsonObject server1 = new JsonObject();
		server1.addProperty("name", "Test-Server");
		server1.addProperty("ip", "1.1.1.1");
		serverlist.add(server1);
		configobject.add("serverlist", serverlist);
		this.Save();
		*/
		
	}
	public void Save(){
		PrintWriter writer;
		try {
			writer = new PrintWriter(ConfigFile);
			writer.print(configobject.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<ConfigServer> GetServerList(){
		ArrayList<ConfigServer> list = new ArrayList<ConfigServer>();
		for(int i = 0; i < configobject.getAsJsonArray("serverlist").size(); i++){
			JsonObject serverentry = configobject.getAsJsonArray("serverlist").get(i).getAsJsonObject();
			String name = serverentry.get("name").getAsString();
			String ip = serverentry.get("ip").getAsString();
			list.add(new ConfigServer(name,ip));
		}
		return list;
	}
	public void AddServer(ConfigServer server){
		JsonObject obj = new JsonObject();
		obj.addProperty("name", server.GetName());
		obj.addProperty("ip", server.GetIP());
		configobject.getAsJsonArray("serverlist").add(obj);
		Save();
	}
	public void DeleteServer(int index){
		configobject.getAsJsonArray("serverlist").remove(index);
		Save();
	}
}

package de.oszimt.vysioneer.Server.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerManager extends Thread {
	ServerSocket server;
	
 	public SocketServerManager(int port) throws IOException{
 		try {
 			server = new ServerSocket(port);	
 		} catch (Exception e){
 			//firewall error????
 		}

 	}
 	public void run(){
       Socket socket = null;
 	   while (true) {
           try {
               socket = server.accept();
           } catch (IOException e) {
               System.out.println("I/O error: " + e);
           }
           // new thread for a client
           new SocketClient(socket).start();
       }
 		
 	}
}

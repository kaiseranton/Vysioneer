package de.oszimt.vysioneer.Client.SocketClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.oszimt.vysioneer.Client.ClientMain;

public class SocketClientManager extends Thread{

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream out;
	private boolean closeconnection = false;
	private boolean wassuccessfully = false;
	private boolean ispincorrect = false;
	private boolean isDone = false;
	private SocketClientType type;
	private int pin;
	
	public SocketClientManager(String ip, int port,int p){
		type = SocketClientType.AUTH;
		pin = p;
		CreateConnection(ip,port);
		this.start();
	}
	public SocketClientManager(String ip, int port){		
		type = SocketClientType.PING;
		CreateConnection(ip,port);
		this.start();
	}
	private void CreateConnection(String ip, int port){
		try {
			socket = new Socket();
			socket.setSoTimeout(200);
			socket.connect(new InetSocketAddress(ip, port), 200);

			while(!socket.isConnected()){
				System.out.println("WAIT");
			}

			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			input = new ObjectInputStream(socket.getInputStream());


		} catch (Exception e){

		}
	}
	public void run(){
		try {
			while(!closeconnection){        	
				Object obj = input.readObject();
				String result = (String)obj;
				if(result.equalsIgnoreCase("Ping")){
					if(type == SocketClientType.PING){
						out.writeObject((String)"Pong");
						out.flush();
						System.out.println("Client: Got Ping sending Pong");
					} else if(type == SocketClientType.AUTH){
						out.writeObject((String)"AUTH");
						out.flush();
						System.out.println("Client: Got Ping sending AUTH");
					} else {
						System.out.println("FALSE TYPE!");
					}

				}
				if(result.equalsIgnoreCase("bye")){
					out.writeObject((String)"Bye");
					out.flush();
					System.out.println("Client: Got Bye going offline..");
					wassuccessfully = true;
					closeconnection = true;
				}
				if(result.equalsIgnoreCase("code?")){
					out.writeObject((String)(this.pin + ""));
					out.flush();
					System.out.println("Client: Got code request sending code");
				}
				if(result.equalsIgnoreCase("PinOK")){
					ispincorrect = true;
					out.writeObject(ClientMain.name);
					out.flush();
					System.out.println("Client: Pin is OK! Sending Name...");
				}
				if(result.equalsIgnoreCase("PinError")){
					ispincorrect = false;
					out.writeObject((String)"Bye");
					out.flush();
					System.out.println("Client: Pin is false! Sending Bye...");
					closeconnection = true;
				}
				if(result.equalsIgnoreCase("AlreadyConnected")){
					ispincorrect = false;
					out.writeObject((String)"Bye");
					out.flush();
					System.out.println("Client: Pin is true but already connected! Sending Bye...");
					closeconnection = true;
				}
			}
		} catch (Exception e){
			//e.printStackTrace();
			//System.out.println("an error");
		} finally {
			try {
				out.close();
				input.close();
				socket.close();
				System.out.println("Disconnected!");
				isDone = true;
			} catch (Exception e){
				isDone = true;
			}
			
		}
		
	}
	public Socket GetSocket(){
		return socket;
	}
	public boolean IsDone(){
		return isDone;
	}
	public boolean WasSuccessfully(){
		return wassuccessfully;
	}
	public boolean IsPinCorrect(){
		return ispincorrect;
	}
}

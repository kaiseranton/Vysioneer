package de.oszimt.vysioneer.Server.SocketServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.oszimt.vysioneer.Server.ServerMain;
import de.oszimt.vysioneer.Server.Client.Client;


public class SocketClient extends Thread {
	protected Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream out;
	private boolean closeconnection = false;
	private boolean nextiscode = false;
	private boolean pinwasok = false;

	public SocketClient(Socket clientSocket) {
		this.socket = clientSocket;
	}
	public void run() {
		System.out.println("GOT CLIENT!");
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			input = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			return;
		}

		try {
			out.writeObject((String)"Ping");
			out.flush();
			System.out.println("Server: New Client sending Ping");

			while(!closeconnection){

				Object obj = input.readObject();
				String result = (String)obj;

				if(nextiscode){
					nextiscode = false;
					if(result.equalsIgnoreCase(ServerMain.pm.GetPin().GetPin() + "")){
						System.out.println("pin ok!");
						out.writeObject((String)"PinOK");
						out.flush();
						pinwasok = true;
					} else {
						System.out.println("pin error!");
						out.writeObject((String)"PinError");
						out.flush();
					}
				} else {
					if(result.equalsIgnoreCase("pong")){
						System.out.println("Got good connection!");
						out.writeObject((String)"Bye");
						out.flush();
					}
					if(result.equalsIgnoreCase("bye")){
						System.out.println("Got bye!");
						closeconnection = true;
					}
					if(result.equalsIgnoreCase("auth")){
						out.writeObject((String)"code?");
						out.flush();
						System.out.println("Got auth! Requesting code");
						nextiscode = true;
					}
					if(pinwasok){
						pinwasok = false;
						String ip = socket.getInetAddress().toString().replaceAll("/", ""); //idk why /
						String name = result;
						boolean exist = false;
						for(int i = 0; i < ServerMain.cm.GetClients().size(); i++){
							if(ServerMain.cm.GetClients().get(i).GetName().equalsIgnoreCase(name) && ServerMain.cm.GetClients().get(i).GetIP().equalsIgnoreCase(ip))
								exist = true;
						}
						if(exist){
							ServerMain.cm.AddClient(new Client(name,ip));
							ServerMain.smc.RefreshClientList();
							out.writeObject((String)"Bye");
							out.flush();
						} else {
							out.writeObject((String)"AlreadyConnected");
							out.flush();
						}
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Got error");
		} finally {
			try {
				out.close();
				input.close();
				socket.close();
				System.out.println("Disconnected!");
			} catch (Exception e){
				//useless
			}

		}



	}
}
package de.oszimt.vysioneer.Client.Display;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import de.oszimt.vysioneer.Client.ClientMain;


public class DisplayServer extends Thread{
	ServerSocket ss = null;
	Vector<ClientThread> clientList =new Vector<ClientThread>();
	long sleepInterval = 125;
	public DisplayServer(long interval) throws Exception {
		ss = new ServerSocket(ClientMain.SERVER_VIDEO_PORT); //eig 2020
		sleepInterval = interval;
		this.setPriority(MIN_PRIORITY);
		this.start();
		startServer();
	}
	private void startServer() {
		Socket client = null;
		System.err.println("Starts listening for clients");
		ClientThread ct = null;
		while(true) {
			try {
				client = ss.accept();
				ct = new ClientThread(client);
				clientList.addElement(ct);
				ct.start();
			} catch(Exception ex) {ex.printStackTrace();}
		}
	}
	private void removeMe(Socket s) {
		clientList.removeElement(s);
	}
	class ClientThread extends Thread {
		Socket client = null;
		ObjectOutputStream os = null;
		InputStream is = null;
		public ClientThread(Socket s) {
			client = s;
			try {
				os = new ObjectOutputStream(s.getOutputStream());
				is = s.getInputStream();
				System.out.println("Client from "+s.getInetAddress().getHostAddress()+" connected");
			} catch(Exception ex) {ex.printStackTrace();}
		}
		public void run() {
			java.awt.image.BufferedImage img = null;
			Robot r = null;
			try { r = new Robot(); } catch(Exception ex) {ex.printStackTrace();}
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle rect = new Rectangle(0,0,size.width, size.height);
			javax.swing.ImageIcon icon = null;
			while(true) {
				try {
					System.gc();
					img = r.createScreenCapture(rect);
					if(false){ // if block show
						img = r.createScreenCapture(new Rectangle(0,0,1,1));
					}
					icon = new javax.swing.ImageIcon(img);
					os.reset();
					os.writeObject(icon);
					os.flush();
					icon = null;
					System.gc();
					try {Thread.currentThread().sleep(sleepInterval);} catch(Exception e) {}
				} 
				catch(Exception ex) { 
					closeAll();
					break;
				}
			} 
		}
		private void closeAll() {
			DisplayServer.this.removeMe(client);
			try {
				os.close();
				is.close();
				client.close();
			} catch(Exception ex) { ex.printStackTrace(); }
		}
	}

}

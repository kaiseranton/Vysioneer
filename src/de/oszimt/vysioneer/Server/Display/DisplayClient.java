package de.oszimt.vysioneer.Server.Display;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

import de.oszimt.vysioneer.Server.ServerMain;

public class DisplayClient  extends Thread implements KeyListener, MouseListener{
	
	Socket s;;
	ObjectInputStream ois;;
	JLabel l = new JLabel();
	JWindow win = new JWindow();
	ImageIcon icon = new ImageIcon();
	JFrame fr = new JFrame();
	String add = null;
	
	public DisplayClient(String ip) {
		try {
			s = new Socket(ip, ServerMain.SERVER_VIDEO_PORT);
			s.getOutputStream();
			ois = new ObjectInputStream(s.getInputStream());
			fr.setTitle("Displaying "+ip+" port " + ServerMain.SERVER_VIDEO_PORT);
			fr.addWindowListener(new WindowCloser());
			fr.getContentPane().add(l);
			l.setIcon(icon);
			Dimension d = fr.getToolkit().getScreenSize();
			fr.setSize(300*d.width/d.height,300);
			fr.addKeyListener(this);
			win.addMouseListener(this);
			fr.setVisible(true);
			this.start();
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}
	public void run() {
		Dimension d = null;
		BufferedImage i = null;
		while(true) {
			try {
				d = fr.getContentPane().getSize();
				icon = (ImageIcon)ois.readObject();
				if(d == null || icon == null) continue;
				if(d.width>0 && d.height>0 && (d.width != icon.getIconWidth() || d.height != icon.getIconHeight())) 
					icon.setImage(icon.getImage().getScaledInstance(d.width, d.height, i.SCALE_FAST));
				l.setIcon(icon);
				l.validate();
				fr.validate();
			} catch(Exception ex) { ex.printStackTrace(); }
		}
	}
	private void SwitchWindowMode() {
		if(fr.isVisible()) {
			fr.setVisible(false);
			fr.getContentPane().removeAll();
			win.getContentPane().removeAll();
			win.getContentPane().add(l);
			win.setSize(win.getToolkit().getScreenSize());
			win.setVisible(true);
			win.requestFocusInWindow();
		}
		else {
			win.setVisible(false);
			win.getContentPane().removeAll();
			fr.getContentPane().removeAll();
			fr.getContentPane().add(l);
			fr.setVisible(true);
			fr.requestFocus();
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		SwitchWindowMode();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		int code = arg0.getKeyCode();
		if(code == KeyEvent.VK_F)
			SwitchWindowMode();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			try { s.close(); } catch(Exception ex) { ex.printStackTrace(); }
			System.exit(0);
		}
	}
}

package de.oszimt.vysioneer.Server.Security;

import java.util.Random;

public class Pin {
	
	private int pin;
	private long endtime;
	private Random random;
	
	public Pin(){
		random = new Random();
		this.Renew();
	}
	
	public long GetEndTime(){
		return endtime;
	}
	public int GetSeconds(){
		return (int)(System.currentTimeMillis() - endtime);
	}
	public void Renew(){
		endtime = System.currentTimeMillis();
		pin = 10000000 + random.nextInt(90000000);
	}
	public int GetPin(){
		return pin;
	}
}

package de.oszimt.vysioneer.Server.Security;


import java.util.Timer;
import java.util.TimerTask;

import de.oszimt.vysioneer.Server.ServerMain;
public class PinManager {
	
	private Pin pin;
	
	public PinManager(){
		pin = new Pin();
		Timer timer = new Timer();
		timer.schedule(new PinTimer(), 0, 60000); // eig 60000
	}
	public Pin GetPin(){
		return pin;
	}
}

class PinTimer extends TimerTask {
    public void run() {
    	ServerMain.pm.GetPin().Renew();
    	ServerMain.smc.SetPin(ServerMain.pm.GetPin().GetPin() + "");
    }
}

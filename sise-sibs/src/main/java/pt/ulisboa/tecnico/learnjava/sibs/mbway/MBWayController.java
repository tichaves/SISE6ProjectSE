package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Random;

public class MBWayController {
	private MBWay model;
	private MBWayView view;
	
	public MBWayController(MBWay model, MBWayView view) {
		this.model = model;
		this.view = view;
	}

	public String getUserPhoneNumber() {
		return model.getPhoneNumber();
	}

	public void setUserPhoneNumber(String phoneNumber) {
		model.setPhoneNumber(phoneNumber);;
	}

	public String getUserIban() {
		return model.getUserIban();
	}

	public void setUserIban(String userIban) {
		model.setUserIban(userIban);
	}
	
	public void updateView(String message) {
		view.printUserMessage(message);
	}
	
	
	
	
	public void associateMbWay(String userIban, String phoneNumber) {
		setUserIban(userIban);
		setUserPhoneNumber(phoneNumber);
		
		int code = new Random().nextInt(1000000);
		updateView("Code: " + String.format("%06d", code) + " (don’t share it with anyone)");
	}

}

package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Random;

public class MBWayController {
	private MBWayModel model;
	private MBWayView view;
	
	private boolean runningState = true; 
	
	public MBWayController(MBWayModel model, MBWayView view) {
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

	public boolean setUserIban(String userIban) {
		return model.setUserIban(userIban);
	}
	
	public void updateView(String message) {
		view.printUserMessage(message);
	}
	
	public void stopRunning() {
		this.runningState = false;
	}
	
	public userInput(String input) {
		switch(userAction) {
		
		case input.contains("associate-mbway"):
			


			associateMbWay(input.substring(16,25), input.substring(beginIndex, endIndex));
		
			continue;
	
		case "confirm-mbway":
		
			continue;
	
		case "mbway-transfer":
		
			continue;
		
		case "mbway-split-bill":
		
			continue;
		
		case "exit": // Finish actions
			stopRunning();
			break;
		
		default:
			System.out.println("Invalid action!");
	
		}
	}
	
	public boolean isRunning() {
		return runningState;
	}
	
	
	public void associateMbWay(String userIban, String phoneNumber) {
		String message;
		
		setUserPhoneNumber(phoneNumber);
		
		if (!setUserIban(userIban)) {
			message = "The iban " + userIban + " already exists!";
		}
		
		int code = new Random().nextInt(1000000);
		updateView("Code: " + String.format("%06d", code) + " (don’t share it with anyone)");
	}

}

package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayController {
	private MBWayModel model;
	private MBWayView view;
	
	private String message;
	
	private boolean runningState;
	
	public MBWayController(MBWayModel model, MBWayView view) {
		this.model = model;
		this.view = view;
		this.runningState = true;
	}
	
	public void updateView(String input) throws NumberFormatException, SibsException, AccountException, OperationException {
		String[] inputs = userInput(input);
		
		switch(inputs[0]) {
		
		case "associate-mbway":
			
			associateMbWay(inputs[1], inputs[2]);
		
			break;
	
		case "confirm-mbway":
			
			confirmMbWay(inputs[1]);
		
			break;
	
		case "mbway-transfer":
			
			transferMbWay(inputs[1], inputs[2], Integer.parseInt(inputs[3]));
		
			break;
		
//		case "mbway-split-bill":
//			splitBill(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
		case "friend":
			
			enterFriends(inputs[1], Integer.parseInt(inputs[2]));
			
			break;
		
		case "exit": // Finish actions
			turnOff();
			view.printUserMessage("You exit the app.");
			break;
		
		default:
			view.printUserMessage("Invalid action!");
	
		}
	}

	public String[] userInput(String input) {
		return input.split(" ");
	}
	
	public boolean isOn() {
		return runningState;
	}
	
	public void turnOff() {
		this.runningState = false;
	}
	
	public void associateMbWay(String phoneNumber, String userIban) {
		this.message = ("Code: " + model.setValues(userIban, phoneNumber)
			+ " (don’t share it with anyone)");
		view.printUserMessage(this.message);
	}
	
	public void confirmMbWay(String code) {
		this.message = "Wrong confirmation code. Try association again.";
		if (model.confirmCode(code)) {
			this.message = "MBWAY association confirmed successfully!";
		}
		
		view.printUserMessage(this.message);
	}
	
	public void transferMbWay(String sourcePhNumber, String targetPhNumber, int amount) throws SibsException, AccountException, OperationException {
		if(!(model.getPhoneNumber().equals(sourcePhNumber) || model.isActive(targetPhNumber))) {
			this.message = "Wrong phone number.";
		} else if(model.getBalance(sourcePhNumber) < amount) {
			this.message = "Not enough money on the source account.";
		} else {
			model.getMBWay().transfer(sourcePhNumber, targetPhNumber, amount);
			this.message = "Transfer performed successfully!";
		}
		view.printUserMessage(this.message);
	}
	
	private void enterFriends(String phoneNumber, int amount) throws NumberFormatException, SibsException, AccountException, OperationException {
		Scanner myObj = new Scanner(System.in);
		HashMap<String, Integer> friendNAmount = new HashMap<>();
		friendNAmount.put(phoneNumber, amount);
		view.printUserMessage("Enter your friends (split bill command to finish):");
//		Boolean isTrue = true;
//		view.printUserMessage("Enter your friends data (\"end\" to finish):");
		while(true) {
			String[] inputs = userInput(myObj.nextLine());;
			switch(inputs[0]) {
			case "friends":
				if (!model.isActive(inputs[1])) {
					view.printUserMessage("Friend " + inputs[1] + " is not registered.");
					continue;
				}
				friendNAmount.put(inputs[1], Integer.parseInt(inputs[2]));
				continue;
				
			case "mbway-split-bill":
				splitBill(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), friendNAmount);
				break;
			
			default:
				view.printUserMessage("Wrong command.");
			}
			
		}
	}

	private void splitBill(int numbFriends, int totalAmount, HashMap<String, Integer> friendNAmount) throws SibsException, AccountException, OperationException {
		int friendSize = friendNAmount.size();
		int sumAmounts = 0;
		for (int value : friendNAmount.values()) {
			sumAmounts += value;
		}
		friendNAmount.remove(model.getPhoneNumber());
		if (numbFriends < friendSize) {
			view.printUserMessage("Oh no! " + friendSize + " friend(s) are missing.");
		} else if (numbFriends > friendSize) {
			view.printUserMessage("Oh no! Too many friends.");
		} else if (totalAmount != sumAmounts) {
			view.printUserMessage("Something is wrong. Did you set the bill amount right?");
		} else {
			if (allHaveMoney(friendNAmount)) {
				for (String friend : friendNAmount.keySet()) {
					transferMbWay(friend, model.getPhoneNumber(), friendNAmount.get(friend));
				}
				view.printUserMessage("Bill payed successfully!");
			}
			
		}
		
	}

	private boolean allHaveMoney(HashMap<String, Integer> friendNAmount) {
		for (String friend : friendNAmount.keySet()) {
			if (model.getBalance(friend) < friendNAmount.get(friend)) {
				view.printUserMessage("Oh no! One friend does not have money to pay!");
				return false;
			}
		}
		return true;
	}
	
}

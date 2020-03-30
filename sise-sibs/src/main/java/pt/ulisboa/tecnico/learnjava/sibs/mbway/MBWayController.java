package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Map;
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

		case "friend":
			
			enterFriends(inputs[1], Integer.parseInt(inputs[2]));
			
			break;
		
		case "exit": // Finish actions
			turnOff();
			this.message = ("You exit the app.");
			break;
		
		default:

			this.message = ("Invalid action!");
	
		}
		view.printUserMessage(this.message);
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
		try {
			this.message = "Code: " + model.setValues(userIban, phoneNumber)
				+ " (don’t share it with anyone)";
		} catch (Exception e) {
			this.message = "Error in \"associateMBWay\"! This Iban does not exist.";
		}
	}
	
	public void confirmMbWay(String code) {
		this.message = "Wrong confirmation code. Try association again.";
		if (model.confirmCode(code)) {
			this.message = "MBWAY association confirmed successfully!";
		}
	}
	
	public void transferMbWay(String sourcePhNumber, String targetPhNumber, int amount) throws SibsException, AccountException, OperationException {
		if(!model.getPhoneNumber().equals(sourcePhNumber) || !model.isActive(targetPhNumber)) {
			this.message = "Wrong phone number.";
		} else if(model.getBalance(sourcePhNumber) < amount) {
			this.message = "Not enough money on the source account.";
		} else {
			model.getMBWay().transfer(sourcePhNumber, targetPhNumber, amount);
			this.message = "Transfer performed successfully!";
		}
	}
	
	private void enterFriends(String phoneNumber, int amount) throws NumberFormatException, SibsException, AccountException, OperationException {
		if (!phoneNumber.equals(model.getPhoneNumber())) {
			view.printUserMessage("The user phone number it is not correct!");
		} else {
			// Refactor for Write Short Units of Code - line 110 to line 115
			Scanner myObj = new Scanner(System.in);
			HashMap<String, Integer> friendNAmount = new HashMap<>();
			friendNAmount.put(phoneNumber, amount);
			view.printUserMessage("Enter your friends (split bill command to finish):");
			runFriends(myObj, friendNAmount);
		}
	}
	
	private void runFriends(Scanner myObj, HashMap<String, Integer> friendNAmount) throws NumberFormatException, SibsException, AccountException, OperationException {
		boolean isTrue = true;
		while(isTrue) {
			String[] inputs = userInput(myObj.nextLine());;
			switch(inputs[0]) {
			case "friend":
				addFriend(inputs, friendNAmount);
				continue;
				
			case "mbway-split-bill":
				splitBill(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), friendNAmount);
				isTrue = false;
				break;
			
			default:
				view.printUserMessage("Wrong command.");
			}
		}
	}
	
	// Refactor for Write Short Units of Code - line 140 to line 147
	private Map<String, Integer> addFriend(String[] inputs, HashMap<String, Integer> friendNAmount) {
		if (!model.isActive(inputs[1])) {
			view.printUserMessage("Friend " + inputs[1] + " is not registered.");
		} else {
			friendNAmount.put(inputs[1], Integer.parseInt(inputs[2]));
		}
		return friendNAmount;
	}
	
	private void splitBill(int numbFriends, int totalAmount, HashMap<String, Integer> friendNAmount) throws SibsException, AccountException, OperationException {
		int friendSize = friendNAmount.size();
		int sumAmounts = 0;
		for (int value : friendNAmount.values()) {
			sumAmounts += value;
		}
		friendNAmount.remove(model.getPhoneNumber());
		if (numbFriends > friendSize) {
			this.message = ("Oh no! " + (numbFriends - friendSize) + " friend(s) are missing.");
		} else if (numbFriends < friendSize) {
			this.message = ("Oh no! Too many friends.");
		} else if (totalAmount != sumAmounts) {
			this.message = ("Something is wrong. Did you set the bill amount right?");
		} else {
			if (allHaveMoney(friendNAmount)) {
				for (String friend : friendNAmount.keySet()) {
					transferMbWay(friend, model.getPhoneNumber(), friendNAmount.get(friend));
				}
				this.message = ("Bill payed successfully!");
			}
		}
	}

	private boolean allHaveMoney(HashMap<String, Integer> friendNAmount) {
		for (String friend : friendNAmount.keySet()) {
			if (model.getBalance(friend) < friendNAmount.get(friend)) {
				this.message = ("Oh no! One friend does not have money to pay!");
				return false;
			}
		}
		return true;
	}
	
}

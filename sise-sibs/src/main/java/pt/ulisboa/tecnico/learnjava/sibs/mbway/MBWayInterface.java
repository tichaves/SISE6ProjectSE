package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayInterface {
	public static MBWay MBWAY = new MBWay();
	
//	public HashMap<Integer, User> users = new HashMap<Integer, User>();
	
//	public static void main(String[] args)  
//    { 
//        User model  = retriveStudentFromDatabase(); 
//  
//        UserView view = new UserView(); 
//  
//        UserController controller = new UserController(model, view); 
//  
//        controller.updateView(); 
//  
//        controller.setStudentName("Vikram Sharma"); 
//  
//        controller.updateView(); 
//    } 
//  
//    private static User retriveUserFromDatabase() 
//    { 
//        User user = new User(); 
//        student.setName("Lokesh Sharma"); 
//        student.setRollNo("15UCS157"); 
//        return student; 
//    } 
	
//  
//  private static MBWayModel retriveUser(String userIban, String phoneNumber) { 
//      MBWayModel user = new MBWayModel(); 
//      user.setUserIban(userIban);
//      user.setPhoneNumber(phoneNumber); 
//      return user; 
//  } 
	
	public static void main(String[] args) throws NumberFormatException, SibsException, AccountException, OperationException {
		
		//fetch student record based on his roll no from the database
//		User model  = retriveUserFromDatabase();
		MBWayModel model = new MBWayModel(MBWAY);

		//Create a view : to write student details on console
		MBWayView view = new MBWayView();
		
		MBWayController controller = new MBWayController(model, view);
		
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		
		System.out.println("Welcome to MBWay App");
		
		while(controller.isOn()) {
			
			String input = myObj.nextLine(); // Read user input
			
//			controller.userInput(input);
			
			controller.updateView(input);
		}
		myObj.close();
	}

}

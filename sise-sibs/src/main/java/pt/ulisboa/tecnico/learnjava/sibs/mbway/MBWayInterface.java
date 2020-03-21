package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

public class MBWayInterface {
	
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
	
	public static void main(String[] args) {
		
		//fetch student record based on his roll no from the database
//		User model  = retriveUserFromDatabase();
		MBWayModel model = new MBWayModel();

		//Create a view : to write student details on console
		MBWayView view = new MBWayView();
		
		MBWayController controller = new MBWayController(model, view);
		
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		
		System.out.println("Welcome to MbWay!\n"
				+ "\n choose one of the following actions.\n");
		
		
		while(controller.isRunning()) {
			
			String input = myObj.nextLine(); // Read user input
			
			controller.userInput(input);
			
			controller.updateView();
		}
		myObj.close();
	}

}

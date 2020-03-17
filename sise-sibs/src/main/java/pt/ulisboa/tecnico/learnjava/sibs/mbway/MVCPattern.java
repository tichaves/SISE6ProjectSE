package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Scanner;

public class MVCPattern {
	
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
	
  
  private static MBWay retriveUser(String userIban, String phoneNumber) { 
      MBWay user = new MBWay(); 
      user.setUserIban(userIban);
      user.setPhoneNumber(phoneNumber); 
      return user; 
  } 
	
	public static void main(String[] args) {
		
		//fetch student record based on his roll no from the database
//		User model  = retriveUserFromDatabase();
		MBWay model = new MBWay();

		//Create a view : to write student details on console
		MBWayView view = new MBWayView();
		
		MBWayController controller = new MBWayController(model, view);
		
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		
		System.out.println("Welcome to MbWay!\n"
				+ "\n choose one of the following actions.\n");
		
		
		while(true) {
			
			System.out.println("Valide Actions: \n" + "		Action '1': Associate a MbWay;\n"
					+ "		Action '2': Confirm MbWay;\n" + "		Action '3': MbWay Transfer;\n"
					+ "		Action '4': MbWay Split Bill;\n"
					+ "		Action 'exit': Finish actions.\n" + "Enter a valide action: ");

			String userAction = myObj.nextLine(); // Read user input
			
			switch(userAction) {
			
			case "1":
				
				System.out.println("Enter your IBAN: ");
				String userIban = myObj.nextLine();
				System.out.println("Enter your phone number: ");
				String phoneNumber = myObj.nextLine();
				
//				//fetch student record based on his roll no from the database
//				User model  = new User();
//
//				//Create a view : to write student details on console
//				UserView view = new UserView();
//				
//				UserController controller = new UserController(model, view);
//				
				controller.associateMbWay(userIban, phoneNumber);
				
				continue;
			
			case "2":
				
				continue;
			
			case "3":
				
				continue;
				
			case "4":
				
				continue;
				
			case "exit": // Finish actions
				System.out.println("You exit the app. (Your app should terminate the program successfully.");
				break;
				
			default:
				System.out.println("Invalid action!");
			
			}
		}
	}

}

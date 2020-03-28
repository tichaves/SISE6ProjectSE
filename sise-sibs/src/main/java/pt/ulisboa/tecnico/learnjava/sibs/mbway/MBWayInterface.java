package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWayAccount;
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
	
	public static void main(String[] args) throws NumberFormatException, SibsException, AccountException, OperationException, BankException, ClientException {
		
		//fetch student record based on his roll no from the database
//		User model  = retriveUserFromDatabase();
		retriveDataFromDatabase();
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
	
	private static void retriveDataFromDatabase() throws BankException, ClientException, AccountException {
		Bank bank = new Bank("cgd");
		Client client1 = new Client(bank, "Bonifacio", "Jacobino", "123456789", "966696669", "Algures perdido", 24);
		Client client2 = new Client(bank, "Aquilino", "Andarilho", "012345678", "933393336", "Algures procurando", 29);
		Client client3 = new Client(bank, "Felizmino", "Tristemino", "233223323", "965432198", "Rua d'Ele", 25);
		String iban1 = bank.createAccount(AccountType.CHECKING, client1, 1000, 1000);
		String iban2 = bank.createAccount(AccountType.CHECKING, client2, 1000, 1000);
		String iban3 = bank.createAccount(AccountType.CHECKING, client3, 1000, 1000);
		MBWayAccount account1 = new MBWayAccount(iban1, bank.getAccountByAccountId(iban1.substring(3)));
		MBWayAccount account2 = new MBWayAccount(iban2, bank.getAccountByAccountId(iban2.substring(3)));
		MBWayAccount account3 = new MBWayAccount(iban3, bank.getAccountByAccountId(iban3.substring(3)));
		account1.setActive();
		account2.setActive();
		account3.setActive();
//		MBWay mbway = new MBWay("962770474", account1);
//		mbway.addFriend("912345678", account2);
//		mbway.addFriend("931234567", account3);
//		return mbway;

	}

}

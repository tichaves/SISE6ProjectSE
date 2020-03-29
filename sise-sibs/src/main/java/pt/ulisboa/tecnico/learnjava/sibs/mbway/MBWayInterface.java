package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWay;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayInterface {
	public static MBWay MBWAY = new MBWay();
	
	public static void main(String[] args) throws NumberFormatException, SibsException, AccountException, OperationException, BankException, ClientException {
		
		//fetch student record based on his roll no from the database
		retriveDataFromDatabase();
		MBWayModel model = new MBWayModel();

		//Create a view : to write student details on console
		MBWayView view = new MBWayView();
		
		MBWayController controller = new MBWayController(model, view);
		
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		
		System.out.println("Welcome to MBWay App");
		
		while(controller.isOn()) {
			
			String input = myObj.nextLine(); // Read user input
			
			controller.updateView(input);
		}
		myObj.close();
	}
	
	private static void retriveDataFromDatabase() throws BankException, ClientException, AccountException {
		Bank bank = new Bank("cgd");
		Client eu = new Client(bank, "Eu", "Pessoa", "212121314", "999999999", "Viaduto Duarte Pacheco", 29);
		Client client1 = new Client(bank, "Bonifacio", "Jacobino", "123456789", "966696669", "Algures perdido", 24);
		Client client2 = new Client(bank, "Aquilino", "Andarilho", "012345678", "933393336", "Algures procurando", 29);
		Client client3 = new Client(bank, "Felizmino", "Tristemino", "233223323", "965432198", "Rua d'Ele", 25);
		String iban = bank.createAccount(AccountType.CHECKING, eu, 1000, 1000);
		String iban1 = bank.createAccount(AccountType.CHECKING, client1, 1000, 1000);
		String iban2 = bank.createAccount(AccountType.CHECKING, client2, 1000, 1000);
		String iban3 = bank.createAccount(AccountType.CHECKING, client3, 1000, 1000);
		String code1 = MBWAY.setValues("966696669", iban1);
		String code2 = MBWAY.setValues("933393336", iban2);
		String code3 = MBWAY.setValues("965432198", iban3);
		MBWAY.confirmCode("966696669", iban1, code1);
		MBWAY.confirmCode("933393336", iban2, code2);

	}

}

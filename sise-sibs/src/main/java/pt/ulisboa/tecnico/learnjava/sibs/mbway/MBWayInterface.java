package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
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
	
	// Refactor for Write Short Units of Code - line 46
	private static void retriveDataFromDatabase() throws BankException, ClientException, AccountException {
		Bank bank = new Bank("cgd");
		Client[] clients = createClients(bank,createPersons());
		String iban = bank.createAccount(AccountType.CHECKING, clients[0], 1000, 1000);
		String iban1 = bank.createAccount(AccountType.CHECKING, clients[1], 1000, 1000);
		String iban2 = bank.createAccount(AccountType.CHECKING, clients[2], 1000, 1000);
		String iban3 = bank.createAccount(AccountType.CHECKING, clients[3], 1000, 1000);
		String code1 = MBWAY.setValues("966696669", iban1);
		String code2 = MBWAY.setValues("933393336", iban2);
		MBWAY.confirmCode("966696669", iban1, code1);
		MBWAY.confirmCode("933393336", iban2, code2);
	}
	
	private static Client[] createClients(Bank bank, Person[] persons) throws ClientException {
		Client[] clients = new Client[4];
		clients[0] = new Client(bank, persons[0], "999999999", "Viaduto Duarte Pacheco");
		clients[1] = new Client(bank, persons[1], "966696669", "Algures perdido");
		clients[2] = new Client(bank, persons[2], "933393336", "Algures procurando");
		clients[3] = new Client(bank, persons[3], "965432198", "Rua d'Ele");
		return clients;
	}
	
	private static Person[] createPersons() throws ClientException {
		Person[] persons = new Person[4];
		persons[0] = new Person("Eu", "Pessoa", "212121314", 29);
		persons[1] = new Person("Bonifacio", "Jacobino", "123456789", 24);
		persons[2] = new Person("Aquilino", "Andarilho", "012345678", 29);
		persons[3] = new Person("Felizmino", "Tristemino", "233223323", 25);
		return persons;
	}

}

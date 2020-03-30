package pt.ulisboa.tecnico.learnjava.bank.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class Client {
	private final Set<Account> accounts = new HashSet<Account>();

	private final Bank bank;
	private Person person;
	private final String phoneNumber;
	private final String address;
	
	// Refactor for Keep Unit Interfaces Small - all Person class and line 46
	public Client(Bank bank, Person person, String phoneNumber, String address)
			throws ClientException {
		checkParameters(bank, person, phoneNumber);
		
		this.bank = bank;
		this.person = person;
		this.phoneNumber = phoneNumber;
		this.address = address;

		bank.addClient(this);
	}

	private void checkParameters(Bank bank, Person person, String phoneNumber) throws ClientException {
		
		if (phoneNumber.length() != 9 || !phoneNumber.matches("[0-9]+")) {
			throw new ClientException();
		}

		if (bank.getClientByNif(person.getNif()) != null) {
			throw new ClientException();
		}
	}

	public void addAccount(Account account) throws ClientException {
		if (this.accounts.size() == 5) {
			throw new ClientException();
		}

		this.accounts.add(account);
	}

	public void deleteAccount(Account account) {
		this.accounts.remove(account);
	}

	public boolean hasAccount(Account account) {
		return this.accounts.contains(account);
	}

	public int getNumberOfAccounts() {
		return this.accounts.size();
	}

	public Stream<Account> getAccounts() {
		return this.accounts.stream();
	}

	public void happyBirthDay() throws BankException, AccountException, ClientException {
		this.person.increaseAge();

		if (this.person.getAge() == 18) {
			Set<Account> accounts = new HashSet<Account>(this.accounts);
			for (Account account : accounts) {
				YoungAccount youngAccount = (YoungAccount) account;
				youngAccount.upgrade();
			}
		}
	}

	public boolean isInactive() {
		return this.accounts.stream().allMatch(a -> a.isInactive());
	}

	public int numberOfInactiveAccounts() {
		return (int) this.accounts.stream().filter(a -> a.isInactive()).count();
	}

	public Bank getBank() {
		return this.bank;
	}
	
	public Person getPerson() {
		return person;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

}

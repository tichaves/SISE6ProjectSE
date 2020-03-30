package pt.ulisboa.tecnico.learnjava.bank.domain;

public class Person {
	private final String firstName;
	private final String lastName;
		private final String address;
	
	public Person(String firstName, String lastName, String address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}
	
	public String getAddress() {
		return this.address;
	}
}

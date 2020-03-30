package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class Person {
	private final String firstName;
	private final String lastName;
	private final String nif;
	private int age;
	
	public Person(String firstName, String lastName, String nif, int age) throws ClientException {
		checkParameters(nif, age);
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.nif = nif;
		this.age = age;
	}
	
	private void checkParameters(String nif, int age) throws ClientException {
		if (age < 0) {
			throw new ClientException();
		}

		if (nif.length() != 9 || !nif.matches("[0-9]+")) {
			throw new ClientException();
		}
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}
	
	public String getNif() {
		return this.nif;
	}
	
	public int getAge() {
		return this.age;
	}

	public void increaseAge() {
		this.age++;
	}
	
}

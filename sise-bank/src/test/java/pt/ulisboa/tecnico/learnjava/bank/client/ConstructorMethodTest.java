package pt.ulisboa.tecnico.learnjava.bank.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class ConstructorMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private static final int AGE = 33;

	private Bank bank;

	@Before
	public void setUp() throws BankException {
		this.bank = new Bank("CGD");
	}

	@Test
	public void success() throws ClientException {
		Person person = new Person(FIRST_NAME, LAST_NAME, NIF, AGE);

		Client client = new Client(this.bank, person, PHONE_NUMBER, ADDRESS);

		assertEquals(this.bank, client.getBank());
		assertEquals(FIRST_NAME, client.getPerson().getFirstName());
		assertEquals(LAST_NAME, client.getPerson().getLastName());
		assertEquals(NIF, client.getPerson().getNif());
		assertEquals(PHONE_NUMBER, client.getPhoneNumber());
		assertEquals(ADDRESS, client.getAddress());
		assertTrue(this.bank.isClientOfBank(client));
	}

	@Test(expected = ClientException.class)
	public void negativeAge() throws ClientException {
		Person person = new Person(FIRST_NAME, LAST_NAME, "12345678A", -1);

		new Client(this.bank, person, PHONE_NUMBER, ADDRESS);
	}

	@Test(expected = ClientException.class)
	public void no9DigitsNif() throws ClientException {
		Person person = new Person(FIRST_NAME, LAST_NAME, "12345678A", AGE);

		new Client(this.bank, person, PHONE_NUMBER, ADDRESS);
	}

	@Test(expected = ClientException.class)
	public void no9DigitsPhoneNumber() throws ClientException {
		Person person = new Person(FIRST_NAME, LAST_NAME, NIF, AGE);

		new Client(this.bank, person, "A87654321", ADDRESS);
	}

	public void twoClientsSameNif() throws ClientException {
		Person person = new Person(FIRST_NAME, LAST_NAME, NIF, AGE);

		new Client(this.bank, person, "A87654321", ADDRESS);
		try {
			new Client(this.bank, person, "A87654321", ADDRESS);
			fail();
		} catch (ClientException e) {
			assertEquals(1, this.bank.getTotalNumberOfClients());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

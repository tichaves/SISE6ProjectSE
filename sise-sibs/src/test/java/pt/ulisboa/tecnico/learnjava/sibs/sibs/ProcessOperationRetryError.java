package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.state.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.state.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.state.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.state.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.state.Error;

public class ProcessOperationRetryError {
	private static final String FIRST_NAME = "Bonifacio";
	private static final String LAST_NAME = "Jacobino";
	private static final String NIF = "123456789";
	private static final String PHONE_NUMBER = "966696669";
	private static final String ADDRESS = "Algures perdido";
	private static int AGE = 24;

	private static final String FIRST_NAME_TWO = "Aquilino";
	private static final String LAST_NAME_TWO = "Andarilho";
	private static final String NIF_TWO = "012345678";
	private static final String PHONE_NUMBER_TWO = "933393336";
	private static final String ADDRESS_TWO = "Algures procurando";
	private static int AGE_TWO = 29;
	
	private static final int VALUE = 1000;
	
	private Services service;
	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Person sourcePerson;
	private Person targetPerson;
	private Client sourceClient;
	private Client targetClientDiffBank;
	private Client targetClientSameBank;
	private String sourceIban;
	private String targetIbanDiffBank;
	private String targetIbanSameBank;

	@Before
	public void setUp() throws BankException, ClientException, AccountException, SibsException, OperationException {
		this.service = new Services();
		this.sibs = new Sibs(5, this.service);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourcePerson = new Person(FIRST_NAME, LAST_NAME, NIF, AGE);
		this.sourceClient = new Client(this.sourceBank, this.sourcePerson, PHONE_NUMBER, ADDRESS);
		this.targetPerson = new Person(FIRST_NAME_TWO, LAST_NAME_TWO, NIF_TWO, AGE_TWO);
		this.targetClientDiffBank = new Client(this.targetBank, this.targetPerson, PHONE_NUMBER_TWO, ADDRESS_TWO);
		this.targetClientSameBank = new Client(this.sourceBank, this.targetPerson, PHONE_NUMBER_TWO, ADDRESS_TWO);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, VALUE, 0);
		this.targetIbanDiffBank = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClientDiffBank, VALUE, 0);
		this.targetIbanSameBank = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.targetClientSameBank, VALUE, 0);
	}
	
	@Test
	public void processError() throws OperationException, SibsException, AccountException {
		this.sibs.transfer(this.sourceIban, this.targetIbanDiffBank, 1001);
		
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);

	}
	
	@Test
	public void twoProcessError() throws OperationException, SibsException, AccountException {
		this.sibs.transfer(this.sourceIban, this.targetIbanDiffBank, 1001);
		
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.transfer(this.targetIbanSameBank, this.targetIbanDiffBank, 1001);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		assertTrue(((TransferOperation) this.sibs.getOperation(1)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
		assertTrue(((TransferOperation) this.sibs.getOperation(1)).getState() instanceof Retry);
	}

	@Test
	public void cancelProcessRetry() throws OperationException, SibsException, AccountException {
		this.sibs.transfer(this.sourceIban, this.targetIbanDiffBank, 1001);
		
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		this.sibs.cancelOperation(0);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Canceled);
	}
	
	@Test
	public void tryCancelError() throws OperationException, SibsException, AccountException {
		this.sibs.transfer(this.sourceIban, this.targetIbanDiffBank, 1001);
		
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();

		this.sibs.cancelOperation(0);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	}

	@Test
	public void tryProcessError() throws OperationException, SibsException, AccountException {
		this.sibs.transfer(this.sourceIban, this.targetIbanDiffBank, 1001);
		
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
		
		this.sibs.processOperations();

		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
		
	}
	
	@Test
	public void processDepositedError() throws OperationException, SibsException, AccountException {
		this.sibs.transfer(this.sourceIban, this.targetIbanDiffBank, 1000);
		
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Withdrawn);
		assertEquals(0, this.service.getAccountByIban(this.sourceIban).getBalance());
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Deposited);
		assertEquals(2000, this.service.getAccountByIban(this.targetIbanDiffBank).getBalance());
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Retry);
		this.sibs.processOperations();
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
		
		assertEquals(1000, this.service.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(1000, this.service.getAccountByIban(this.targetIbanDiffBank).getBalance());

	}
	
	@After
	public void tearDown() {
		this.sibs = null;
		Bank.clearBanks();
	}
}

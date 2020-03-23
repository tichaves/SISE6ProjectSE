package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.state.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.state.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.state.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.state.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.state.Withdrawn;

public class TransferOperationStateTests {
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
	
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClientDiffBank;
	private Client targetClientSameBank;
	private String sourceIban;
	private String targetIbanDiffBank;
	private String targetIbanSameBank;
	private Services service;

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.service = new Services();
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, AGE);
		this.targetClientDiffBank = new Client(this.targetBank, FIRST_NAME_TWO, LAST_NAME_TWO, NIF_TWO, PHONE_NUMBER_TWO, ADDRESS_TWO, AGE_TWO);
		this.targetClientSameBank = new Client(this.sourceBank, FIRST_NAME_TWO, LAST_NAME_TWO, NIF_TWO, PHONE_NUMBER_TWO, ADDRESS_TWO, AGE_TWO);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, VALUE, 0);
		this.targetIbanDiffBank = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClientDiffBank, VALUE, 0);
		this.targetIbanSameBank = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.targetClientSameBank, VALUE, 0);
	}

	
	@Test
	public void transferSameBank() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanSameBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertEquals(900, this.service.getAccountByIban(sourceIban).getBalance());
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertEquals(1100, this.service.getAccountByIban(targetIbanSameBank).getBalance());
		assertTrue(operation.getState() instanceof Completed);
		
	}
	
	@Test
	public void transferDiffBanks() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertEquals(900, this.service.getAccountByIban(sourceIban).getBalance());
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertEquals(1100, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
		assertTrue(operation.getState() instanceof Deposited);
		operation.process();
		assertEquals(900-6, this.service.getAccountByIban(sourceIban).getBalance());
		assertTrue(operation.getState() instanceof Completed);

	}
	
	@Test
	public void registered2Cancel() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.cancel();
		
		try {
			operation.process();
			fail();
		} catch (OperationException e) {
			assertTrue(operation.getState() instanceof Canceled);
			assertEquals(1000, this.service.getAccountByIban(sourceIban).getBalance());
			assertEquals(1000, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
			System.out.println(e.getType());
		}
	}
	
	@Test
	public void withdrawn2Cancel() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertEquals(900, this.service.getAccountByIban(sourceIban).getBalance());
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.cancel();
		
		try {
			operation.process();
			fail();
		} catch (OperationException e) {
			assertTrue(operation.getState() instanceof Canceled);
			assertEquals(1000, this.service.getAccountByIban(sourceIban).getBalance());
			assertEquals(1000, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
			System.out.println(e.getType());
		}
	}
	
	@Test
	public void deposited2Cancel() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertEquals(900, this.service.getAccountByIban(sourceIban).getBalance());
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertEquals(1100, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
		assertTrue(operation.getState() instanceof Deposited);
		operation.cancel();
		
		try {
			operation.process();
			fail();
		} catch (OperationException e) {
			assertTrue(operation.getState() instanceof Canceled);
			assertEquals(1000, this.service.getAccountByIban(sourceIban).getBalance());
			assertEquals(1000, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
			System.out.println(e.getType());
		}
	}
	
	@Test
	public void completedTryCancel() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getState() instanceof Deposited);
		operation.process();
		assertTrue(operation.getState() instanceof Completed);
		
		try {
			operation.cancel();
			fail();
		} catch (OperationException e) {
			assertTrue(operation.getState() instanceof Completed);
			assertEquals(900-6, this.service.getAccountByIban(sourceIban).getBalance());
			assertEquals(1100, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
			System.out.println(e.getType());
		}
	}
	
	@Test
	public void completedTryProcess() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getState() instanceof Deposited);
		operation.process();
		assertTrue(operation.getState() instanceof Completed);
		
		try {
			operation.process();
			fail();
		} catch (OperationException e) {
			assertTrue(operation.getState() instanceof Completed);
			assertEquals(900-6, this.service.getAccountByIban(sourceIban).getBalance());
			assertEquals(1100, this.service.getAccountByIban(targetIbanDiffBank).getBalance());
			System.out.println(e.getType());
		}
	}
	
	@Test
	public void canceledTryCancel() throws OperationException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIbanDiffBank, 100);
		assertTrue(operation.getState() instanceof Registered);
		operation.cancel();
		
		try {
			operation.cancel();
			fail();
		} catch (OperationException e) {
			assertTrue(operation.getState() instanceof Canceled);
			System.out.println(e.getType());
		}
	}
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}
	
}

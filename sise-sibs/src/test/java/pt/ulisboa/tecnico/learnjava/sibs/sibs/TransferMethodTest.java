package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.state.Registered;

public class TransferMethodTest {

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
	private Client sourceClient;
	private Client targetClientSameBank;
	private String sourceIban;
	private String targetIbanSameBank;

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.service = new Services();
		this.sibs = new Sibs(3, this.service);
		this.sourceBank = new Bank("CGD");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, AGE);
		this.targetClientSameBank = new Client(this.sourceBank, FIRST_NAME_TWO, LAST_NAME_TWO, NIF_TWO, PHONE_NUMBER_TWO, ADDRESS_TWO, AGE_TWO);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, VALUE, 0);
		this.targetIbanSameBank = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.targetClientSameBank, VALUE, 0);
	}

	@Test
	public void success() throws OperationException, SibsException, AccountException, BankException, ClientException {
		this.sibs.transfer(this.sourceIban, this.targetIbanSameBank, 100);
		
		assertTrue(this.sibs.getOperation(0) instanceof TransferOperation);
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Registered);
        assertEquals(1, this.sibs.getNumberOfOperations());
        assertEquals(100, this.sibs.getTotalValueOfOperations());
        assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
        assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}
	
	@After
	public void tearDown() {
		this.sibs = null;
		Bank.clearBanks();
	}

}

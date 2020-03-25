package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.state.Error;

public class RetryMockito {
	private Sibs sibs;
	 private static final int AMOUNT = 100;
	 private static final String SOURCE_IBAN = "ABCDE";
	 private static final String TARGET_IBAN = "FGHIJ";
	 
	 @Test
	 public void errorRegistered() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class);
		 this.sibs = new Sibs(3, servicesMock);
		 
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(false);

		 doThrow(AccountException.class).when(servicesMock).withdraw(SOURCE_IBAN, AMOUNT);
		 
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		 
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 
		 verify(servicesMock, times(3)).withdraw(SOURCE_IBAN, AMOUNT);
		 verify(servicesMock, never()).deposit(TARGET_IBAN, AMOUNT);
		 assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	 }

	 @Test
	 public void errorWithdrawn() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class);
		 this.sibs = new Sibs(3, servicesMock);
		 
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(false);

		 doThrow(AccountException.class).when(servicesMock).deposit(TARGET_IBAN, AMOUNT);
		 
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		 
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 
		 verify(servicesMock, times(1)).withdraw(SOURCE_IBAN, AMOUNT);
		 verify(servicesMock, times(3)).deposit(TARGET_IBAN, AMOUNT);
		 assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Error);
	 }

	 @After
	 public void tearDown() {
		 this.sibs = null;
		 Bank.clearBanks();
	 }
}

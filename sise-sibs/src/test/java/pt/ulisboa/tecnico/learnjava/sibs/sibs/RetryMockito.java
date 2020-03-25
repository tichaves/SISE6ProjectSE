package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class RetryMockito {
	private Sibs sibs;
	 private static final int AMOUNT = 100;
	 private static final String SOURCE_IBAN = "ABCDE";
	 private static final String TARGET_IBAN = "FGHIJ";
	 private static final int COMISSION = 6;
	 
	 @Test //SUCCESS - 4 - Transfer between two accounts in the same bank (without fees)
	 public void success() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class); // start a mock of Service class
		 Account accountMock = mock(Account.class);
		 this.sibs = new Sibs(3, servicesMock);
		 
		 when(servicesMock.getAccountByIban(SOURCE_IBAN)).thenReturn(accountMock);
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(false); 
		 when(accountMock.getBalance()).thenReturn(AMOUNT-1);
		 
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		 
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 
		 verify(servicesMock, times(1)).withdraw(SOURCE_IBAN, AMOUNT);
		 verify(servicesMock, times(1)).deposit(TARGET_IBAN, AMOUNT);
		 
		 verify(accountMock, never()).withdraw(AMOUNT);
	 }
	 

	 @After
	 public void tearDown() {
		 this.sibs = null;
		 Bank.clearBanks();
	 }
}

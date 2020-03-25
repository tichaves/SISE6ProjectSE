package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import static org.mockito.Mockito.*;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTestMockito {
	 private Sibs sibs;
	 private static final int AMOUNT = 100;
	 private static final String SOURCE_IBAN = "ABCDE";
	 private static final String TARGET_IBAN = "FGHIJ";
	 private static final int COMISSION = 6;
	 
	 @Test
	 public void successSameBanks() throws BankException, AccountException, SibsException, OperationException, ClientException {
		 Services servicesMock = mock(Services.class);
		 this.sibs = new Sibs(3, servicesMock);
		 
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(false); 
		 
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		 
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 
		 verify(servicesMock).withdraw(SOURCE_IBAN, AMOUNT);
		 verify(servicesMock).deposit(TARGET_IBAN, AMOUNT);
	     
		 assertEquals(1, this.sibs.getNumberOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		 assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		 
	 }
	    
	 @Test
	 public void successDifferentBanks() throws BankException, AccountException, SibsException, OperationException, ClientException {
		 Services servicesMock= mock(Services.class); 
		 this.sibs = new Sibs(3, servicesMock);
		 
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(true);
	    	
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		 
		 this.sibs.processOperations();
		 this.sibs.processOperations();
		 this.sibs.processOperations();
	     this.sibs.processOperations();
		 
		 verify(servicesMock).withdraw(SOURCE_IBAN, AMOUNT);
		 verify(servicesMock).withdraw(SOURCE_IBAN, COMISSION);
		 verify(servicesMock).deposit(TARGET_IBAN, AMOUNT);
	        
		 assertEquals(1, this.sibs.getNumberOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		 assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		 
	 }
	    
	 @Test
	 public void accountSourceExists() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class); 
		 this.sibs = new Sibs(3, servicesMock);
	    
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(false);
		 
		 try {
			 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
			 fail();
		 } catch (Exception e) {
			 verify(servicesMock, never()).withdraw(SOURCE_IBAN, AMOUNT);
			 verify(servicesMock, never()).deposit(TARGET_IBAN, AMOUNT);
		 }
	 }
	    
	 @Test
	 public void accountTargetInactive() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class);
		 this.sibs = new Sibs(3, servicesMock);
	    
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(false);
	    	
		 when(servicesMock.accountInactive(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountInactive(TARGET_IBAN)).thenReturn(false);
	    	
		 try {
			 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
			 fail();
		 } catch (Exception e) {
			 verify(servicesMock, never()).withdraw(SOURCE_IBAN, AMOUNT);
			 verify(servicesMock, never()).deposit(TARGET_IBAN, AMOUNT);
		 }
	 }
	 
	 @After
	 public void tearDown() {
		 this.sibs = null;
		 Bank.clearBanks();
	 }

}

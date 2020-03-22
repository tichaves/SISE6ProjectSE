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
	 private static final String SOURCE_BANK_CODE ="RRR";
	 private static final String TARGET_BANK_CODE ="SSS";
	 private static final int COMISSION = 6;
	 
	 @Test //SUCCESS - 4 - Transfer between two accounts in the same bank (without fees)
	 public void success() throws BankException, AccountException, SibsException, OperationException, ClientException {
		 Services servicesMock = mock(Services.class); // start a mock of Service class
		 this.sibs = new Sibs(AMOUNT, servicesMock);
		 
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(false); 
		 
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
	      
		 verify(servicesMock).withdraw(SOURCE_IBAN, AMOUNT);
		 verify(servicesMock).deposit(TARGET_IBAN, AMOUNT);
	        
		 assertEquals(1, this.sibs.getNumberOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		 assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		 
	 }
	    
	 @Test //SUCCESS DIFFERENT BANKS- 5 - Transfer between two accounts in different banks (with fees)
	 public void successDifferentBanks() throws BankException, AccountException, SibsException, OperationException, ClientException {
		 Services servicesMock= mock(Services.class); 
		 this.sibs = new Sibs(AMOUNT, servicesMock);
		 
		 when(servicesMock.accountExists(SOURCE_IBAN)).thenReturn(true);
		 when(servicesMock.accountExists(TARGET_IBAN)).thenReturn(true);
		 
		 when(servicesMock.diffBanks(SOURCE_IBAN, TARGET_IBAN)).thenReturn(true); 
//			when(servicesMock.getBankCodeByIban(TARGET_IBAN)).thenReturn(TARGET_BANK_CODE);
	    	
		 this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
	      
		 verify(servicesMock).withdraw(SOURCE_IBAN, AMOUNT + COMISSION);
		 verify(servicesMock).deposit(TARGET_IBAN, AMOUNT);
	        
		 assertEquals(1, this.sibs.getNumberOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperations());
		 assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		 assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		 
	 }
	    
	 @Test // Transfer for an inexistent account - 6
	 public void accountSourceExists() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class); 
		 this.sibs = new Sibs(AMOUNT, servicesMock);
	    
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
	    
	 @Test // Transfer for an inactive account - 6
	 public void accountTargetInactive() throws SibsException, AccountException, OperationException {
		 Services servicesMock = mock(Services.class); //criar um mock e passa-lo a classe Sibs
		 this.sibs = new Sibs(AMOUNT, servicesMock);
	    
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

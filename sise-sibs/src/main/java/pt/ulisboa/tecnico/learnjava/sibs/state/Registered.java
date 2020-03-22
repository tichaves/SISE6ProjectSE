package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Registered implements TransferState {
	@Override
	public void process(TransferOperation operation) throws OperationException, AccountException {
//		// Do the withdraw in the account
//		operation.getService().withdraw(operation.getSourceIban(), operation.getValue());
		
		// Set the state to withdraw
		operation.setState(new Withdrawn());
	}
	
	@Override
	public void cancel(TransferOperation operation) throws OperationException {
		operation.setState(new Canceled());
	}
}

package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Deposited implements TransferState {
	@Override
	public void process(TransferOperation operation, Services service) throws OperationException, AccountException {
		// Do the withdraw in the source account
		service.withdraw(operation.getSourceIban(), operation.commission());
		
		// Set the next state
		operation.setState(new Completed());
	}
	
	@Override
	public void cancel(TransferOperation operation, Services service) throws OperationException, AccountException {
		// Do the withdraw in the target account
		service.withdraw(operation.getTargetIban(), operation.getValue());
		// Do the deposit in the source account
		service.deposit(operation.getSourceIban(), operation.getValue());
		
		// Set the next state
		operation.setState(new Canceled());
	}
}

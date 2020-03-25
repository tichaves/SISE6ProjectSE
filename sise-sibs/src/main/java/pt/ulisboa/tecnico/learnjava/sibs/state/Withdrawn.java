package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Withdrawn implements TransferState {
	@Override
	public void process(TransferOperation operation, Services service) throws OperationException, AccountException {
		// Do the deposit in the target account
		service.deposit(operation.getTargetIban(), operation.getValue());
		
		// Set the next state
		if (service.diffBanks(operation.getSourceIban(), operation.getTargetIban())) {
			operation.setState(new Deposited());
		} else {
			operation.setState(new Completed());
		}
	}
	
	@Override
	public void cancel(TransferOperation operation, Services service) throws OperationException, AccountException {
		// Do the deposit in the source account
		service.deposit(operation.getSourceIban(), operation.getValue());
		
		operation.setState(new Canceled());
	}
}

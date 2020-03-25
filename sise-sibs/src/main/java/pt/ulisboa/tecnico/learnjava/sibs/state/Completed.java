package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Completed implements TransferState {
	@Override
	public void process(TransferOperation operation, Services service) throws OperationException {
		throw new OperationException("Error in \"process\" method! "
				+ "The operation is already in the state \"Completed\".");
	}
	
	@Override
	public void cancel(TransferOperation operation, Services service) throws OperationException {
		throw new OperationException("Error in \"cancel\" method! You can "
				+ "not cancel a transfer operation in the state \"Completed\".");
	}
}

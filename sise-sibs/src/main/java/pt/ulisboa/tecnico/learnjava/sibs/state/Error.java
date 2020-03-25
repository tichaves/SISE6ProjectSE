package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Error implements TransferState {

	@Override
	public void process(TransferOperation operation, Services service) throws OperationException, AccountException {
		throw new OperationException("Error in \"process\" method! You can not process an operation "
				+ "in the state \"Error\".");
	}

	@Override
	public void cancel(TransferOperation operation, Services service) throws OperationException, AccountException {
		throw new OperationException("Error in \"cancel\" method! You can not cancel an operation "
				+ "in the state \"Error\".");
	}

}

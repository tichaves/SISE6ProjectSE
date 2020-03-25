package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public interface TransferState {
	void process(TransferOperation operation, Services service) throws OperationException, AccountException;
	void cancel(TransferOperation operation, Services service) throws OperationException, AccountException;
}
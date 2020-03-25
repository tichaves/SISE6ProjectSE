package pt.ulisboa.tecnico.learnjava.sibs.state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Retry implements TransferState {
	private int lifes = 3;
	private TransferState previewState = null;
	
	@Override
	public void process(TransferOperation operation, Services service) throws OperationException, AccountException {
		
		if (!(operation.getState() instanceof Retry)) {
			this.previewState = operation.getState();
			operation.setState(this);
		} else {
			if (lifes > 0) {
				processPreviewState(operation, service);
			} else {
				this.previewState.cancel(operation, service);
				operation.setState(new Error());
			}
		}
		this.lifes -= 1;
		
	}
	
	@Override
	public void cancel(TransferOperation operation, Services service) throws OperationException, AccountException {
		this.previewState.cancel(operation, service);
	}
	
	public void processPreviewState(TransferOperation operation, Services service) {
		try {
			this.previewState.process(operation, service);
		} catch (Exception e) {
			System.out.println("Retry process.");
		}
	}
}

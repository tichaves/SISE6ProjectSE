package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.state.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.state.TransferState;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	
//	private String state;
	private TransferState currentState;
	private Services service;

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		
		// Part 2 - Ex.1 
//		this.state = "Registered";
		this.currentState = new Registered();
		this.service = new Services();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}
	
	public void setState(TransferState state) {
		this.currentState = state;
	}
	
	public TransferState getState() {
		return this.currentState;
	}
	
	public Services getService() {
		return this.service;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}
	
//	public void process() throws AccountException, OperationException {
//		if (this.state.equals("Canceled")) {
//			throw new OperationException("Error in process! You can not process a 'Canceled' operation.");
//		}
//		
//		if (this.state.equals("Registered")) {
//			setState("Withdrawn");
//		} else if (this.state.equals("Withdrawn") && this.service.diffBanks(this.sourceIban, this.targetIban)) {
//			setState("Deposited");
//		} else {
//			setState("Completed");
//		}
//	}
	
	public void process() throws OperationException, AccountException {
		currentState.process(this);
	}
	
//	public void cancel() throws OperationException {
//		if (this.state.equals("Completed")) {
//			throw new OperationException("Error in cancel! You can not cancel a transfer operation in '"
//					+ this.state + "' state.");
//		}
//		setState("Canceled");
//	}
	
	public void cancel() throws OperationException {
		currentState.cancel(this);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}
	
//	public String getState() {
//		return this.state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}

}

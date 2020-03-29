package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWay {
	private HashMap<String, MBWayAccount> users = new HashMap<>();
	private Services service;
	private Sibs sibs;
	
	private int numbOp = 100;
	
	public MBWay() {
		this.service = new Services();
		this.sibs = new Sibs(numbOp, service);
	}
	
	public String setValues(String phoneNumber, String userIban) {
		MBWayAccount account =  new MBWayAccount(userIban, this.service.getAccountByIban(userIban));
		if (this.users.containsKey(phoneNumber)) {
			this.users.replace(phoneNumber, account);
		} else {
			this.users.put(phoneNumber, account);
		}
		return account.getCode();
	}
	
	public Boolean confirmCode(String phoneNumber, String userIban, String code) {
		return this.users.get(phoneNumber).confirmCode(userIban, code);
	}

	public Boolean activeMbWayAccount(String phoneNumber) {
		try {
			return this.users.get(phoneNumber).isActive();
		} catch (Exception e) {
			return false;
		}
	}

	public int getBalance(String sourcePhNumber) {
		return this.users.get(sourcePhNumber).getAccount().getBalance();
	}
	
	public MBWayAccount getMBWayAccount(String iban) {
		return this.users.get(iban);
	}

	public void transfer(String sourcePhNumber, String targetPhNumber, int amount) throws SibsException, AccountException, OperationException {
		String sourceIban = this.users.get(sourcePhNumber).getIban();
		String targetIban = this.users.get(targetPhNumber).getIban();
		this.sibs.transfer(sourceIban, targetIban, amount);
	}
	
	
	
}

package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;

public class MBWayAccount {
	private Account account;
	private String iban;
	private String code;
	private Boolean active = false;
	
	public MBWayAccount(String iban, Account account) {
		this.account = account;
		this.iban = iban;
		this.code = String.format("%06d", (int) (new Random()).nextInt(1000000));
	}

	public Account getAccount() {
		return account;
	}

	public String getIban() {
		return iban;
	}
	
	protected void setActive() {
		this.active = true;
	}
	
	public Boolean isActive() {
		return this.active;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public Boolean confirmCode(String iban, String code) {
		if (iban.equals(this.iban) && code.equals(this.code)) {
			setActive();
			return true;
		}
		return false;
	}
}

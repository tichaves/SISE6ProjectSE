package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWay;

public class MBWayModel {
	private MBWay mbway;
	private String phoneNumber;
	private String userIban;
	
	public MBWayModel(MBWay mbway) {
		this.mbway = mbway;
	}

	public String setValues(String phoneNumber, String userIban) {
		this.phoneNumber = phoneNumber;
		this.userIban = userIban;
		
		return mbway.setValues(phoneNumber, userIban);
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public MBWay getMBWay() {
		return this.mbway;
	}

	public Boolean confirmCode(String code) {
		return this.mbway.confirmCode(this.phoneNumber, this.userIban, code);
	}
	
	public Boolean isActive(String phoneNumber) {
		return mbway.activeMbWayAccount(phoneNumber);
	}

	public int getBalance(String sourcePhNumber) {
		return this.mbway.getBalance(sourcePhNumber);
	}
	
}

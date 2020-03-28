package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWay;

public class MBWayModel {
	private MBWay mbway;
	private String phoneNumber;
	private String userIban;
//	private MBWayAccount mbWayAccount;
//	private Services service = new Services();
	
//	private String code;
	
//	private HashMap<String, Set<String>> myMBWay = new HashMap<>();
//	private Set<String> ibans = new HashSet<>();
	
	private Set<String> friendsPhones = new HashSet<>();
	
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
	
//	public String getUserIban() {
//		return this.mbWayAccount.getIban();
//	}
	
//	public boolean setUserIban(String userIban) {
//		return this.ibans.add(userIban);
//	}
	

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

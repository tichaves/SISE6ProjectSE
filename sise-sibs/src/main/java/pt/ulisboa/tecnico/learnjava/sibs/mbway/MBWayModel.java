package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MBWayModel {
	private String phoneNumber;
	private String userIban;
	
//	private HashMap<String, Set<String>> myMBWay = new HashMap<>();
	private Set<String> ibans = new HashSet<>();
	
	private Set<String> friendsPhones = new HashSet<>();
	
	public boolean setValues(String phoneNumber, String iban) {
		setPhoneNumber(phoneNumber);
		
		this.ibans.add(iban);
		this.myMBWay.put(phoneNumber, this.ibans);
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getUserIban() {
		return userIban;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		if (!myMBWay.containsKey(phoneNumber)) {
			this.phoneNumber = phoneNumber;
			myMBWay.put(this.phoneNumber, ibans);
		}
	}
	
	public boolean setUserIban(String userIban) {
		return this.ibans.add(userIban);
	}
	
}

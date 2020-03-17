package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MBWay {
	private String phoneNumber;
	private String userIban;
	
	private HashMap<Integer, Set<String>> myMBWay = new HashMap<>();
	private Set<String> ibans = new HashSet<>();
	
	private Set<Integer> friendsPhones = new HashSet<>();
	
	public void setValues(int phoneNumber, String iban) {
		this.ibans.add(iban);
		this.myMBWay.put(phoneNumber, this.ibans);
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getUserIban() {
		return userIban;
	}
	
//	public void setPhoneNumber(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//	}
//	
//	public void setUserIban(String userIban) {
//		this.userIban = userIban;
//	}
	
}

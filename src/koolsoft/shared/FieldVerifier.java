package koolsoft.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class FieldVerifier {
	public static int isValidSearchKeyword(String name) {
		
		if (name == null || name.isEmpty() ) {
			return -1; // empty
		}
		else if  (name.length() > 3) return 1; // >3
		return 0; // between 1 and 3 ?
	}

	public static Boolean isValidName(String name) {
		
		if (name == null || name.length() > 15 || name.length() < 2) {
			Window.alert("Error: Name "+name+" is invalid, min 2 , max 15 char");
//			GWT.log("Error: Name "+name+" is invalid");
			return false; //not ok 
		}
//		GWT.log("Success: Name "+name+" is valid");
		return true; //oke 
		
	}
	
	
	public static Boolean isValidPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.length() > 10 || phoneNumber.length() < 8) {
			Window.alert("Error: phoneNumber "+phoneNumber+" is invalid,  min 8 , max 10 char");
//			GWT.log("Error: phoneNumber "+phoneNumber+" is invalid");
			return false; //not ok  
		}
//		GWT.log("Success: phoneNumber "+phoneNumber+" is valid");
		return true; //oke 
		
	}
	
	public static Boolean isValidAddress(String address) {
		if (address == null || address.length() < 3 || address.length() > 25) {
			Window.alert("Error: address "+address+" is invalid,  min 3 , max 25 char");
//			GWT.log("Error: address "+address+" is invalid");
			return false; //not ok  null / <3 / >15
		}
//		GWT.log("Success: address "+address+" is valid");
		return true; //oke >3 <15
		
	}
}

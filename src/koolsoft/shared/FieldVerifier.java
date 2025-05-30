package koolsoft.shared;

import com.google.gwt.user.client.Window;

import koolsoft.shared.enums.SearchValidationResult;

public class FieldVerifier {
	public static SearchValidationResult isValidSearchKeyword(String name) {
		
		if (name == null || name.isEmpty() ) {
			return SearchValidationResult.EMPTY; // empty
		}
		else if  (name.length() > 3) return SearchValidationResult.VALID; // >3
		return SearchValidationResult.TOO_SHORT; // between 1 and 3 ?
	}

	public static Boolean isValidName(String name) {
		
		if (name == null || name.length() > 15 || name.length() < 2) {
			Window.alert("Error: Name "+name+" is invalid, min 2 , max 15 char");
			return false; //not ok 
		}
		return true; //oke 
		
	}
	
	
	public static Boolean isValidPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.length() > 10 || phoneNumber.length() < 8) {
			Window.alert("Error: phoneNumber is invalid,  at least 8 , max 10 char");
			return false; //not ok  
		}
		if (phoneNumber.contains(" ")) {
			Window.alert("Error: phoneNumber must not contains space letter");
			return false;
		}
		return true; //oke 
		
	}
	
	public static Boolean isValidAddress(String address) {
		if (address == null || address.length() < 3 || address.length() > 25) {
			Window.alert("Error: address is invalid,  min 3 , max 25 char");
			return false; //not ok  null / <3 / >15
		}
		return true; //oke >3 <15
		
	}
}

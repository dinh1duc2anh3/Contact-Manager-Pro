package com.hello.shared.verifier;

import com.google.gwt.user.client.Window;

import com.hello.shared.enums.SearchValidationResult;

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
	
	
	public static Boolean isValidGender(String gd) {
		if (gd == null || gd.isEmpty() ) {
			Window.alert("Error: gender is invalid, selected at least 1 gender");
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
		if (address == null || address.isEmpty() ) {
			Window.alert("Error: address is invalid, selected at least 1 address");
			return false; //not ok  
		}
		return true; //oke 
		
	}
}

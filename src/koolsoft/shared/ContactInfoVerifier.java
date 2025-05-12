package koolsoft.shared;

import com.google.gwt.core.client.GWT;

public class ContactInfoVerifier {
	
	
	public static Boolean contactInfoVerifier(
			String fn, String ln, String pn, String ad) {
		GWT.log("validating contact info ");
		Boolean firstNameAddVerifier = FieldVerifier.isValidName(fn);
		if (!firstNameAddVerifier) {
//			GWT.log("Error: invalid firstName: "+fn);
			return false;
		}
		
		Boolean lastNameAddVerifier = FieldVerifier.isValidName(ln);				
		if (!lastNameAddVerifier) {
//			GWT.log("Error: invalid lastName: "+ln);
			return false;
		}				
		
		Boolean phoneNumberAddVerifier = FieldVerifier.isValidPhoneNumber(pn);				
		if (!phoneNumberAddVerifier) {
//			GWT.log("Error: invalid phoneNumber: "+pn);
			return false;
		}				
		
		Boolean addressAddVerifier = FieldVerifier.isValidAddress(ad);
		if (!addressAddVerifier) {
//			GWT.log("Error: invalid address: "+ad);
			return false;
		}
		
		
		GWT.log("Success: valid contact info ");		
		return true;				
	}
}

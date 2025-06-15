package com.hello.shared.verifier;

import com.google.gwt.core.client.GWT;

public class ContactInfoVerifier {

	public static Boolean contactInfoVerifier(String fn, String ln, String pn, String ad) {
		GWT.log("validating contact info ");
		Boolean firstNameAddVerifier = FieldVerifier.isValidName(fn);
		if (!firstNameAddVerifier) {
			return false;
		}

		Boolean lastNameAddVerifier = FieldVerifier.isValidName(ln);
		if (!lastNameAddVerifier) {
			return false;
		}

		Boolean phoneNumberAddVerifier = FieldVerifier.isValidPhoneNumber(pn);
		if (!phoneNumberAddVerifier) {
			return false;
		}

		Boolean addressAddVerifier = FieldVerifier.isValidAddress(ad);
		if (!addressAddVerifier) {
			return false;
		}

		GWT.log("Success: valid contact info ");
		return true;
	}
}

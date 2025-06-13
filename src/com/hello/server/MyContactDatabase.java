package com.hello.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hello.shared.model.ContactInfo;

public class MyContactDatabase {
	
//
	public static final List<ContactInfo> CONTACTS = new ArrayList<>(
			Arrays.asList(
				      new ContactInfo("mary","smith", "09841 27113", "123 First St"),
				      new ContactInfo("Michael","SMith", "098 4127115", "129 Second Rd"),
				      new ContactInfo("Patricia","Johnson", "0984127117", "133 Fourth Blvd"),
				      new ContactInfo("Barbara","Johnson", "0984127119", "553 Third Ln"),
				      new ContactInfo("John","Williams", "0984127121", "173 Fourth Avenue"),
				      new ContactInfo("Elizabeth","Williams", "0984127123", "134 Fourth Ln"),
				      new ContactInfo("Linda","Michael", "0984127125", "175 Second St"),
				      new ContactInfo("Robert","Michael", "0984127127", "231 Fourth Way"),
				      new ContactInfo("James","William", "0984127129", "345 Third Rd"),
				      new ContactInfo("Linda","William", "0984127131", "129 Peachtree Ln"))
			);

	
}

package koolsoft.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.view.client.ListDataProvider;

public class MyContactDatabase implements MyDB {
	
//
	public static final List<ContactInfo> CONTACTS = new ArrayList<>(
			Arrays.asList(
				      new ContactInfo("Mary","Smith", "0984127113", "123 First St"),
				      new ContactInfo("Michael","Smith", "0984127115", "129 Second Rd"),
				      new ContactInfo("Patricia","Johnson", "0984127117", "133 Fourth Blvd"),
				      new ContactInfo("Barbara","Johnson", "0984127119", "553 Third Ln"),
				      new ContactInfo("John","Williams", "0984127121", "173 Fourth Avenue"),
				      new ContactInfo("Elizabeth","Williams", "0984127123", "134 Fourth Ln"),
				      new ContactInfo("Linda","Michael", "0984127125", "175 Second St"),
				      new ContactInfo("Robert","Michael", "0984127127", "231 Fourth Way"),
				      new ContactInfo("James","William", "0984127129", "345 Third Rd"),
				      new ContactInfo("Linda","William", "0984127131", "129 Peachtree Ln"))
			);

	@Override
	public List<ContactInfo> getAll() {
		return MyContactDatabase.CONTACTS;
	}

	@Override
	public List<ContactInfo> getContactInfoFromName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
//	public static final List<ContactInfo> CONTACTS = Arrays.asList(
//		      new ContactInfo("Mary","Smith", "0984127113", "123 First St"),
//		      new ContactInfo("Michael","Smith", "0984127114", "129 Second Rd"),
//		      new ContactInfo("Patricia","Johnson", "0984127115", "133 Fourth Blvd"),
//		      new ContactInfo("Barbara","Johnson", "0984127116", "553 Third Ln"),
//		      new ContactInfo("John","Williams", "0984127117", "173 Fourth Avenue"),
//		      new ContactInfo("Elizabeth","Williams", "0984127118", "134 Fourth Ln"),
//		      new ContactInfo("Linda","Michael", "0984127119", "175 Second St"),
//		      new ContactInfo("Robert","Michael", "0984127120", "231 Fourth Way"),
//		      new ContactInfo("James","William", "0984127121", "345 Third Rd"),
//		      new ContactInfo("Linda","William", "0984127122", "129 Peachtree Ln"));

//	public static final List<ContactInfo> CONTACTS = Arrays.asList(
//		      new ContactInfo("Mary"),
//		      new ContactInfo("Michael"),
//		      new ContactInfo("Patricia"),
//		      new ContactInfo("Barbara"),
//		      new ContactInfo("John"),
//		      new ContactInfo("Elizabeth"),
//		      new ContactInfo("Linda"),
//		      new ContactInfo("Robert"),
//		      new ContactInfo("James"),
//		      new ContactInfo("Linda"));
	
	
//	public static final List<ContactInfo> CONTACTS = Arrays.asList(
//    new ContactInfo("Mary","Smith"),
//    new ContactInfo("Michael","Smith"),
//    new ContactInfo("Patricia","Johnson"),
//    new ContactInfo("Barbara","Johnson"),
//    new ContactInfo("John","Williams"),
//    new ContactInfo("Elizabeth","Williams"),
//    new ContactInfo("Linda","Michael"),
//    new ContactInfo("Robert","Michael"),
//    new ContactInfo("James","William"),
//    new ContactInfo("Linda","William"));
	
//	public static final List<ContactInfo> CONTACTS = Arrays.asList(
//		      new ContactInfo("Mary","Smith", "0984127113"),
//		      new ContactInfo("Michael","Smith", "0984127114"),
//		      new ContactInfo("Patricia","Johnson", "0984127115"),
//		      new ContactInfo("Barbara","Johnson", "0984127116"),
//		      new ContactInfo("John","Williams", "0984127117"),
//		      new ContactInfo("Elizabeth","Williams", "0984127118"),
//		      new ContactInfo("Linda","Michael", "0984127119"),
//		      new ContactInfo("Robert","Michael", "0984127120"),
//		      new ContactInfo("James","William", "0984127121"),
//		      new ContactInfo("Linda","William", "0984127122"));
//	
	
}

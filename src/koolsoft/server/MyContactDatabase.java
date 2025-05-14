package koolsoft.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.view.client.ListDataProvider;

import koolsoft.server.mapper.ContactInfoMapper;
import koolsoft.shared.ContactInfo;

public class MyContactDatabase {
	
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

	
}

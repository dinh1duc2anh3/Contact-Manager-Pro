package com.hello.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.model.ContactInfo;

public class MyContactDatabase {
	

	public static final List<ContactInfo> CONTACTS = new ArrayList<>(
	        Arrays.asList(
	            new ContactInfo("Robert", "Michael", Gender.MALE, "0984127127", Address.HA_NOI),
	            new ContactInfo("Michael", "SMith", Gender.MALE, "098 4127115", Address.CAN_THO),
	            new ContactInfo("James", "William", Gender.MALE, "0984127129", Address.HO_CHI_MINH),
	            new ContactInfo("Patricia", "Johnson", Gender.MALE, "0984127117", Address.HA_NOI),
	            new ContactInfo("Barbara", "Johnson", Gender.MALE, "0984127119", Address.HO_CHI_MINH),
	            new ContactInfo("John", "Williams", Gender.MALE, "0984127121", Address.DA_NANG),
	            new ContactInfo("Elizabeth", "Williams", Gender.FEMALE, "0984127123", Address.HAI_PHONG),
	            new ContactInfo("Linda", "Michael", Gender.FEMALE, "0984127125", Address.CAN_THO),
	            new ContactInfo("mary", "smith", Gender.FEMALE, "09841 27113", Address.HA_NOI),
	            new ContactInfo("Linda", "William", Gender.FEMALE, "0984127131", Address.CAN_THO)
	        )
	    );
    
	
}

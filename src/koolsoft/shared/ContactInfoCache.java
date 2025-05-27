package koolsoft.shared;

import java.util.ArrayList;
import java.util.List;

public class ContactInfoCache {
	private static List<ContactInfo> currentContacts = new ArrayList<>();

	public static List<ContactInfo> getCurrentContacts() {
		return currentContacts;
	}

	public static void setCurrentContacts(List<ContactInfo> currentContacts) {
		ContactInfoCache.currentContacts = currentContacts;
	}
	
	public static void clear() {
		currentContacts.clear();
	}
	
}

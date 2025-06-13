package com.hello.shared.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.hello.shared.model.ContactInfo;

public class ContactInfoCache {
	private static Map<String, ContactInfo> contactMap = new HashMap<>();

	public static List<ContactInfo> getCurrentContacts() {
		return new ArrayList<>(contactMap.values());
	}

	public static void setCurrentContacts(List<ContactInfo> contacts) {
		contactMap.clear();
		for (ContactInfo contact : contacts) {
			contactMap.put(contact.getPhoneNumber(), contact);
		}
	}
	
	public static ContactInfo getByPhoneNumber(String phoneNumber) {
		return contactMap.get(phoneNumber);
	}
	
	public static void addContact(ContactInfo contact) {
		contactMap.put(contact.getPhoneNumber(), contact);
	}
	
	public static void updateContact(ContactInfo contact) {
		contactMap.put(contact.getPhoneNumber(), contact);
	}
	
	public static void removeContact(ContactInfo contact) {
		contactMap.remove(contact.getPhoneNumber());
	}

	public static void removeContacts(Set<ContactInfo>  selectedContacts) {
		List<String> phones = selectedContacts.stream().map(ContactInfo::getPhoneNumber).collect(Collectors.toList());
		for (String phone : phones) {
			contactMap.remove(phone);
		}
	}
	
	
	public static void clear() {
		contactMap.clear();
	}
}

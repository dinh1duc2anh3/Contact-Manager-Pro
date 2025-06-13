package com.hello.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

import com.hello.shared.model.ContactInfo;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;
/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	
	List<ContactInfo> getContactInfosByFirstName(String firstName) throws ContactNoneExistsException ;
	
	List<ContactInfo> getContactInfosByFullName(String fullName) throws ContactNoneExistsException;
	
	ContactInfo getContactInfosByPhoneNumber(String phoneNumber);
	
	List<ContactInfo> getAllContactInfos() throws IllegalArgumentException;
	
	void addContactInfo(ContactInfo newContact) throws ContactAlreadyExistsException;
	
	void updateContactInfo(ContactInfo selectedContact ,ContactInfo updatedContact) throws ContactAlreadyExistsException,ContactNoneExistsException;
	
	void deleteContactByPhoneNumber(String phoneNumber) throws ContactNoneExistsException;
	
	void deleteContacts(List<String> phoneNumbers) throws ContactNoneExistsException;
}

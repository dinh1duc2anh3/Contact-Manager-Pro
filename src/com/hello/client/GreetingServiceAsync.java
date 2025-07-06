package com.hello.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

import com.hello.shared.exception.ContactNoneExistsException;
import com.hello.shared.model.ContactInfo;
/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void getContactInfoById(Long id,  AsyncCallback<ContactInfo> callback) throws ContactNoneExistsException;
	
	void getContactInfosByFirstName(String firstName, AsyncCallback<List<ContactInfo>> callback);
	
	void getContactInfosByFullName(String fullName,  AsyncCallback<List<ContactInfo>> callback);
	
	void getContactInfosByPhoneNumber(String phoneNumber,  AsyncCallback<ContactInfo> callback);
	
	void startsWithPhoneNumber(String phoneNumber,  AsyncCallback<List<ContactInfo>> callback);
	
	void getAllContactInfos( AsyncCallback<List<ContactInfo>> callback) ;
	
	void addContactInfo(ContactInfo newContact,AsyncCallback<Void> callback);
	
	void updateContactInfo(ContactInfo selectedContact, ContactInfo updatedContact, 
	        AsyncCallback<Void> callback);
	
	void deleteContactByPhoneNumber(String phoneNumber , AsyncCallback<Void> callback) ;
	
	void deleteContactById(Long id, AsyncCallback<Void> callback) throws ContactNoneExistsException;

	void deleteContactsByIds(List<Long> ids, AsyncCallback<Void> callback) throws ContactNoneExistsException;
	
	void deleteContacts(List<String> phoneNumbers, AsyncCallback<Void> callback);
}

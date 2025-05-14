package koolsoft.client.service;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import koolsoft.shared.ContactInfo;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void getContactInfosByFirstName(String firstName, AsyncCallback<List<ContactInfo>> callback);
	
	void getAllContactInfos( AsyncCallback<List<ContactInfo>> callback) ;
	
	void addContactInfo(ContactInfo newContact,AsyncCallback<Void> callback);
	
	
	void updateContactInfo(ContactInfo selectedContact , ContactInfo updatedContact,AsyncCallback<Void> callback) ;
	
	void deleteContactByPhoneNumber(String phoneNumber , AsyncCallback<Void> callback) ;
	
	void deleteContacts(List<String> phoneNumbers, AsyncCallback<Void> callback);
	
}

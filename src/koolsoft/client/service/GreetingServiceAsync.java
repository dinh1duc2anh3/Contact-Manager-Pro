package koolsoft.client.service;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import koolsoft.shared.ContactInfoDTO;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
//	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void checkContactExisted(ContactInfoDTO contact,  AsyncCallback<Boolean> callback) ;
	
	void checkPhoneNumberExisted(String phoneNumber,  AsyncCallback<Boolean> callback) ;
	
	void getContactInfosByFirstName(String firstName, AsyncCallback<List<ContactInfoDTO>> callback);
	
	void getAllContactInfos( AsyncCallback<List<ContactInfoDTO>> callback) ;
	
	void addContactInfo(ContactInfoDTO newContact,AsyncCallback<Void> callback);
	
	
	void updateContactInfo(ContactInfoDTO selectedContact , ContactInfoDTO updatedContact,AsyncCallback<Void> callback) ;
	
	
	void deleteContactByFirstName( String firstName, AsyncCallback<Boolean> callback) ;
	
	void deleteContactByPhoneNumber(String phoneNumber , AsyncCallback<Void> callback) ;
	
	void deleteContacts(List<String> phoneNumbers, AsyncCallback<Void> callback);
	
}

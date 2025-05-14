package koolsoft.client.service;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import koolsoft.shared.ContactInfo;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	
	List<ContactInfo> getContactInfosByFirstName(String firstName) throws ContactNoneExistsException;
	
	List<ContactInfo> getAllContactInfos() throws IllegalArgumentException;
	
	void addContactInfo(ContactInfo newContact) throws ContactAlreadyExistsException;
	
	void updateContactInfo(ContactInfo selectedContact ,ContactInfo updatedContact) throws ContactAlreadyExistsException,ContactNoneExistsException;
	
	void deleteContactByPhoneNumber(String phoneNumber) throws ContactNoneExistsException;
	
	void deleteContacts(List<String> phoneNumbers) throws ContactNoneExistsException;
}

package koolsoft.client.service;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import koolsoft.shared.ContactInfoDTO;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
//	String greetServer(String name) throws IllegalArgumentException;
	Boolean checkContactExisted(ContactInfoDTO contact) throws IllegalArgumentException;
	
	Boolean checkPhoneNumberExisted(String phoneNumber) throws IllegalArgumentException;
	
	List<ContactInfoDTO> getContactInfosByFirstName(String firstName) throws IllegalArgumentException;
	
	List<ContactInfoDTO> getAllContactInfos() throws IllegalArgumentException;
	
	void addContactInfo(ContactInfoDTO newContact) throws ContactAlreadyExistsException;
	
	void updateContactInfo(ContactInfoDTO selectedContact ,ContactInfoDTO updatedContact) throws ContactAlreadyExistsException,ContactNoneExistsException;
	
	Boolean deleteContactByFirstName(String firstName) throws IllegalArgumentException;
	
	void deleteContactByPhoneNumber(String phoneNumber) throws ContactNoneExistsException;
	
	void deleteContacts(List<String> phoneNumbers) throws ContactNoneExistsException;
}

package koolsoft.server;

import java.util.List;

import koolsoft.shared.ContactInfo;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

public interface MyDB {
	List<ContactInfo> getAll();
	void add(ContactInfo contactInfo) throws ContactAlreadyExistsException, ContactNoneExistsException;
	void update(ContactInfo selectedContact, ContactInfo updatedContact) throws ContactAlreadyExistsException, ContactNoneExistsException;
	void delete(List<String> phoneNumbers) throws ContactNoneExistsException;
	void deleteByPhoneNumber(String phoneNumber) throws ContactNoneExistsException;
	List<ContactInfo> findByFirstName(String firstname) throws ContactNoneExistsException;
	
	List<ContactInfo> findByFullName(String fullname) throws ContactNoneExistsException;
	ContactInfo findByPhoneNumber(String phoneNumber) ;
	
}

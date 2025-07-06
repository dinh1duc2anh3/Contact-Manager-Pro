package com.hello.server;

import java.util.List;

import com.hello.shared.model.ContactInfo;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;

public interface MyDB {
	List<ContactInfo> findAll();
	void add(ContactInfo contactInfo) throws ContactAlreadyExistsException, ContactNoneExistsException;
	void update(ContactInfo selectedContact, ContactInfo updatedContact) throws ContactAlreadyExistsException, ContactNoneExistsException;
	void deleteByPhoneNumbers(List<String> phoneNumbers) throws ContactNoneExistsException, ContactAlreadyExistsException;
	void deleteByPhoneNumber(String phoneNumber) throws ContactNoneExistsException, ContactAlreadyExistsException;
	
	void deleteById(Long id) throws ContactNoneExistsException;
	void deleteByIds(List<Long> ids) throws ContactNoneExistsException;
	ContactInfo findById(Long id) throws ContactNoneExistsException;
	List<ContactInfo> findByFirstName(String firstname) throws ContactNoneExistsException ;
	List<ContactInfo> findByFullName(String fullname) throws ContactNoneExistsException;
	ContactInfo findByPhoneNumber(String phoneNumber) throws ContactAlreadyExistsException ;
	List<ContactInfo> startsWithPhoneNumber(String phoneNumber_part);
	
}

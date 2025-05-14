package koolsoft.server;

import java.util.List;

import koolsoft.shared.ContactInfo;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

public class MySQLDB implements MyDB {

	@Override
	public List<ContactInfo> getAll() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void add(ContactInfo contactInfo) throws ContactAlreadyExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ContactInfo selectedContact, ContactInfo updatedContact) throws ContactAlreadyExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> phoneNumbers) throws ContactNoneExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByPhoneNumber(String phoneNumber) throws ContactNoneExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContactInfo findByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactInfo> findByFirstName(String firstname) throws ContactNoneExistsException {
		// TODO Auto-generated method stub
		return null;
	}

}

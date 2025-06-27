package com.hello.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.hello.server.mapper.ContactInfoMapper;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;

public class MyObjectifyDB implements MyDB  {
	
	private static final Logger log = Logger.getLogger(MyObjectifyDB.class.getName());
	
	
	public void loadInit() {
		System.out.println("Loading init...");

	    // 1. Xoá toàn bộ contact cũ khỏi Datastore
	    List<Key<ContactInfo>> keys = ObjectifyService.ofy()
	            .load()
	            .type(ContactInfo.class)
	            .keys()
	            .list();
	    ObjectifyService.ofy().delete().keys(keys).now();
	    System.out.println("Deleted existing contacts: " + keys.size());

	    // 2. Lưu lại dữ liệu mẫu từ MyContactDatabase
	    List<ContactInfo> contacts = MyContactDatabase.CONTACTS;
	    ObjectifyService.ofy().save().entities(contacts).now();
	    System.out.println("Saved " + contacts.size() + " new contacts");

	    // 3. In ra kiểm tra createdDate có bị null không
	    List<ContactInfo> loadedEntities = ObjectifyService.ofy()
	            .load()
	            .type(ContactInfo.class)
	            .list();
	}
	
	public void printAllContacts() {
	    List<ContactInfo> contacts = ObjectifyService.ofy().load().type(ContactInfo.class).order("createdDate").list();
	    
	    List<ContactInfo> contactDTOs = ContactInfoMapper.toDTOList(contacts);
	    System.out.println("Total contacts in Datastore: " + contactDTOs.size());
	    for (ContactInfo contactDTO : contactDTOs) {
	        System.out.println(
	        		contactDTO.getId()+ 
	        		" - " + contactDTO.getFirstName() + 
	        		" - " + contactDTO.getLastName() + 
	        		" - " + contactDTO.getPhoneNumber() + 
	        		" - " + contactDTO.getAddress()+ 
	        		" - " + contactDTO.getCreatedDate()
	        		);
	    }
	}
	
	@Override
	public List<ContactInfo> findAll() {
		System.out.println("Getting all contacts: " );
		
		 List<ContactInfo> contacts = ObjectifyService.ofy().load().type(ContactInfo.class).list();
		
		if (contacts.size() == 0) loadInit();
		
		contacts = ObjectifyService.ofy().load().type(ContactInfo.class).list();
		List<ContactInfo> contactDTOs = ContactInfoMapper.toDTOList(contacts);
		System.out.println("Total contacts get from Datastore: " + contactDTOs.size());
		
		printAllContacts();
		return contactDTOs;
        
	}
	
	@Override
	public List<ContactInfo> findByFirstName(String firstname_format) throws ContactNoneExistsException  {
		
		List<ContactInfo> contacts = ObjectifyService.ofy().load().type(ContactInfo.class)
			    .filter("firstName >=", firstname_format)
			    .filter("firstName <", firstname_format + "\ufffd")
			    .list();
        if (contacts.isEmpty()) {
            throw new ContactNoneExistsException("Can't find contact from firstname: " + firstname_format);
        }
        return ContactInfoMapper.toDTOList(contacts);
        
	}

	@Override
	public List<ContactInfo> findByFullName(String fullname_format) throws ContactNoneExistsException {
		List<ContactInfo> contacts = ObjectifyService.ofy().load().type(ContactInfo.class)
			    .filter("fullName >=", fullname_format)
			    .filter("fullName <", fullname_format + "\ufffd")
			    .list();
        if (contacts.isEmpty()) {
            throw new ContactNoneExistsException("Can't find contact from fullname: " + fullname_format);
        }
        return ContactInfoMapper.toDTOList(contacts);
	}
	
	@Override
	public ContactInfo findByPhoneNumber(String phoneNumber_format) throws ContactAlreadyExistsException  {
		List<ContactInfo> existing = ObjectifyService.ofy().load()
				.type(ContactInfo.class)
				.filter("phoneNumber =",phoneNumber_format)
				.list();
		
		if (existing.size() != 1) throw new ContactAlreadyExistsException("Contact with duplicate phonenumber in system!");
		
		return ContactInfoMapper.toDTO(existing.get(0)) ;
	}
	
	@Override
	public List<ContactInfo> startsWithPhoneNumber(String phoneNumber_format)  {
		List<ContactInfo> contacts = ObjectifyService.ofy().load()
				.type(ContactInfo.class)
				.filter("phoneNumber >=", phoneNumber_format)
			    .filter("phoneNumber <", phoneNumber_format + "\ufffd")
				.list();
		
		return ContactInfoMapper.toDTOList(contacts) ;
	}
	

	@Override
	public void add(ContactInfo contactInfo) throws ContactAlreadyExistsException {
		
		ObjectifyService.ofy().save().entity(contactInfo).now();
		log.info("success added contact: " + contactInfo.getFirstName() + contactInfo.getLastName() + contactInfo.getPhoneNumber());
		return ;
	}
	
	

	@Override
	public void update(ContactInfo selectedContact, ContactInfo updatedContact) throws ContactAlreadyExistsException {

		
		
		System.out.println(this.getClass() + "updating contact: " + selectedContact.getFirstName() + selectedContact.getLastName());
		//assume selected phone number has already existed
		
		// Check if updated phone number is already used by another contact  
		ContactInfo existing = findByPhoneNumber(updatedContact.getPhoneNumber());
		
		if (existing != null && !existing.getPhoneNumber().equals(selectedContact.getPhoneNumber()) ) {
			throw new ContactAlreadyExistsException("Phone number already in use.");
		}
		
		// 2. Lấy contact từ DB bằng id
	    ContactInfo contactInDB = ObjectifyService.ofy().load().type(ContactInfo.class)
	        .id(selectedContact.getId()).now();

	    if (contactInDB == null) {
	        throw new IllegalStateException("Contact not found in DB.");
	    }

	    // 3. Update thông tin
	    contactInDB.setFirstName(updatedContact.getFirstName());
	    contactInDB.setLastName(updatedContact.getLastName());
	    contactInDB.setFullName(); // nếu bạn có
	    contactInDB.setGender(updatedContact.getGender());
	    contactInDB.setAddress(updatedContact.getAddress());
	    contactInDB.setPhoneNumber(updatedContact.getPhoneNumber()); // có thể khác so với ban đầu

	    ObjectifyService.ofy().save().entity(contactInDB).now();
	    

		log.info("Success: Updated contact info of " + selectedContact.getFullName());
		return ;
	}
	
	@Override
	public void deleteByPhoneNumber(String phoneNumber) throws ContactNoneExistsException, ContactAlreadyExistsException {
		//get contact
		ContactInfo existing = findByPhoneNumber(phoneNumber);
		
		if (existing == null) {
			log.severe("Error: Phone number does not exist - " + phoneNumber);
			throw new ContactNoneExistsException("Selected phone number: "+phoneNumber+ " doesn't exist.");
		}
		
		ObjectifyService.ofy().delete().entity(existing).now();
		log.info("Successfully deleted contact info for phone number " + phoneNumber);
		return ;
		
	}

	@Override
	public void delete(List<String> phoneNumbers) throws ContactNoneExistsException, ContactAlreadyExistsException {
		for (String phoneNumber : phoneNumbers) {
			System.out.println("deleting contact info with phoneNumber: " + phoneNumber);
			deleteByPhoneNumber(phoneNumber);
		}
		
		return ;
	}

	
	
	

}

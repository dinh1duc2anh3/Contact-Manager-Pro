package com.hello.server;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;
import com.hello.server.mapper.ContactInfoMapper;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;

public class MyObjectifyDB implements MyDB  {
	
	private static final Logger logger = Logger.getLogger(MyObjectifyDB.class.getName());
	
	
	public void loadInit() {
		System.out.println("Loading init: " );
		List<ContactInfo> contacts = MyContactDatabase.CONTACTS;
		
		ObjectifyService.ofy().save().entities(contacts).now(); 
		
		
		List<ContactInfo> loadedEntities = ObjectifyService.ofy().load().type(ContactInfo.class).list();
        System.out.println("Saved " + loadedEntities.size() + " contacts");
        
        // Map entities to DTOs
        return ;
	}
	
	public void printAllContacts() {
	    List<ContactInfo> contacts = ObjectifyService.ofy().load().type(ContactInfo.class).list();
	    
	    List<ContactInfo> contactDTOs = ContactInfoMapper.toDTOList(contacts);
	    System.out.println("Total contacts in Datastore: " + contactDTOs.size());
	    for (ContactInfo contactDTO : contactDTOs) {
	        System.out.println(contactDTO.getFirstName() + " " + contactDTO.getLastName() + 
	        		" - " + contactDTO.getFirstName()+ " -"  + contactDTO.getFullName()+ 
	        		"- " + contactDTO.getPhoneNumber());
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
		List<ContactInfo> entities = ObjectifyService.ofy().load().type(ContactInfo.class)
                .filter("firstName =", firstname_format)
                .list();
		
		if (entities == null || entities.isEmpty()) {
			throw new ContactNoneExistsException("Can't find contact from firstname: "+firstname_format+ " doesn't exist.");
		}
        return ContactInfoMapper.toDTOList(entities);
        
	}

	@Override
	public List<ContactInfo> findByFullName(String fullname_format) throws ContactNoneExistsException {
		List<ContactInfo> entities = ObjectifyService.ofy().load().type(ContactInfo.class)
                .filter("fullName =", fullname_format)
                .list();
		if (entities == null || entities.isEmpty()) {
			throw new ContactNoneExistsException("Can't find contact from firstname: "+fullname_format+ " doesn't exist.");
		}
		
        return ContactInfoMapper.toDTOList(entities);
	}
	
	@Override
	public ContactInfo findByPhoneNumber(String phoneNumber_format)  {
		ContactInfo existing = ObjectifyService.ofy().load()
				.type(ContactInfo.class)
				.id(phoneNumber_format)
				.now();
		
		return ContactInfoMapper.toDTO(existing) ;
	}

	@Override
	public void add(ContactInfo contactInfo) throws ContactAlreadyExistsException {
		
		
		if (findByPhoneNumber(contactInfo.getPhoneNumber()) != null ) {
			logger.warning("Error: Contact"+ contactInfo.getFirstName() + contactInfo.getLastName() + contactInfo.getPhoneNumber() + "already exists");
			throw new ContactAlreadyExistsException("This contact already exists.");
		}
		
		System.out.println("adding contact: " + contactInfo.getFirstName() + contactInfo.getLastName() + contactInfo.getPhoneNumber());
		ObjectifyService.ofy().save().entity(contactInfo).now();
		return ;
	}

	@Override
	public void update(ContactInfo selectedContact, ContactInfo updatedContact) throws ContactAlreadyExistsException {

		
		
		System.out.println("updating contact: " + selectedContact.getFirstName() + selectedContact.getLastName());
		//assume selected phone number has already existed
		//check if updated phoneNumber has existed in the DB  
		
		ContactInfo existing = findByPhoneNumber(updatedContact.getPhoneNumber());
		
		//existed and !=
		if (existing != null && !updatedContact.getPhoneNumber().equals(selectedContact.getPhoneNumber()) ) {
			throw new ContactAlreadyExistsException("Phone number already in use.");
		}
		
		//not existed and !=
		else if (existing == null && !updatedContact.getPhoneNumber().equals(selectedContact.getPhoneNumber())){
			ObjectifyService.ofy().delete().entity(selectedContact).now();
		}
		
		//exist + =
		ObjectifyService.ofy().save().entity(updatedContact).now();
		logger.info("Success: Updated contact info of " + selectedContact.getFirstName() + " " + selectedContact.getLastName());
		return ;
	}
	
	@Override
	public void deleteByPhoneNumber(String phoneNumber) throws ContactNoneExistsException {
		//get contact
		ContactInfo existing = findByPhoneNumber(phoneNumber);
		
		if (existing == null) {
			logger.severe("Error: Phone number does not exist - " + phoneNumber);
			throw new ContactNoneExistsException("Selected phone number: "+phoneNumber+ " doesn't exist.");
		}
		
		ObjectifyService.ofy().delete().entity(existing).now();
		logger.info("Successfully deleted contact info for phone number " + phoneNumber);
		return ;
		
	}

	@Override
	public void delete(List<String> phoneNumbers) throws ContactNoneExistsException {
		for (String phoneNumber : phoneNumbers) {
			System.out.println("deleting contact info with phoneNumber: " + phoneNumber);
			deleteByPhoneNumber(phoneNumber);
		}
		
		return ;
	}

	
	
	

}

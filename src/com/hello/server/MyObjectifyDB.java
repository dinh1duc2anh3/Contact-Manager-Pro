package com.hello.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;
import com.hello.server.mapper.ContactInfoMapper;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;

public class MyObjectifyDB implements MyDB  {
	
	private static final Logger log = Logger.getLogger(MyObjectifyDB.class.getName());
	
	
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
	        System.out.println(contactDTO.getFirstName() + 
	        		" - " + contactDTO.getLastName() + 
	        		" - " + contactDTO.getPhoneNumber() + 
	        		" - " + contactDTO.getAddress());
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
		List<ContactInfo> allContacts = ObjectifyService.ofy().load().type(ContactInfo.class).list();
        // Filter manually using contains (case-insensitive)
        List<ContactInfo> filtered = new ArrayList<>();
        for (ContactInfo contact : allContacts) {
            if (contact.getFirstName() != null &&
                contact.getFirstName().toLowerCase().contains(firstname_format.toLowerCase())) {
                filtered.add(contact);
            }
        }

        if (filtered.isEmpty()) {
            throw new ContactNoneExistsException("Can't find contact from firstname: " + firstname_format);
        }

        return ContactInfoMapper.toDTOList(filtered);
        
	}

	@Override
	public List<ContactInfo> findByFullName(String fullname_format) throws ContactNoneExistsException {
        List<ContactInfo> allContacts = ObjectifyService.ofy().load().type(ContactInfo.class).list();
        // Filter manually using contains (case-insensitive)
        List<ContactInfo> filtered = new ArrayList<>();
        for (ContactInfo contact : allContacts) {
            if (contact.getFullName() != null &&
                contact.getFullName().toLowerCase().contains(fullname_format.toLowerCase())) {
                filtered.add(contact);
            }
        }

        if (filtered.isEmpty()) {
            throw new ContactNoneExistsException("Can't find contact from fullname: " + fullname_format);
        }

        return ContactInfoMapper.toDTOList(filtered);
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
		
		ObjectifyService.ofy().save().entity(contactInfo).now();
		log.info("success added contact: " + contactInfo.getFirstName() + contactInfo.getLastName() + contactInfo.getPhoneNumber());
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
		log.info("Success: Updated contact info of " + selectedContact.getFirstName() + " " + selectedContact.getLastName());
		return ;
	}
	
	@Override
	public void deleteByPhoneNumber(String phoneNumber) throws ContactNoneExistsException {
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
	public void delete(List<String> phoneNumbers) throws ContactNoneExistsException {
		for (String phoneNumber : phoneNumbers) {
			System.out.println("deleting contact info with phoneNumber: " + phoneNumber);
			deleteByPhoneNumber(phoneNumber);
		}
		
		return ;
	}

	
	
	

}

package koolsoft.server;

import koolsoft.client.service.GreetingService;
import koolsoft.server.mapper.ContactInfoMapper;
import koolsoft.shared.ContactInfoDTO;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private static final Logger logger = Logger.getLogger(GreetingServiceImpl.class.getName());
    
    private MyDB myDB = new MyContactDatabase();

    @Override
    public List<ContactInfoDTO> getContactInfosByFirstName(String input) throws IllegalArgumentException {
//        List<ContactInfoDTO> result = MyContactDatabase.CONTACTS.stream()
//                .filter(c -> c.getFirstName().equalsIgnoreCase(input))
//                .map(ContactInfoMapper::toDTO)
//                .collect(Collectors.toList());
        List<ContactInfoDTO> list = new ArrayList<>();
    	
    	ContactInfo contact = new ContactInfo();
        
        contact.setFirstName("Minh");
        contact.setLastName("Anhh");
        contact.setAddress("Hanoi");
        contact.setPhoneNumber("123123123");
        ofy().save().entity(contact).now(); // Lưu contact

        ContactInfo loaded = ofy().load().type(ContactInfo.class).id(contact.getPhoneNumber()).now(); // Load contact
        ContactInfoDTO sth = ContactInfoMapper.toDTO(loaded);
        
        list.add(sth);
        return list;
//        return result;
    }

    @Override
    public List<ContactInfoDTO> getAllContactInfos() {
        logger.info("Success: get all contacts");
        return myDB.getAll().stream().map(ContactInfoMapper::toDTO).collect(Collectors.toList());
    }


    @Override
	public void addContactInfo(ContactInfoDTO newContact) throws ContactAlreadyExistsException {
    	ContactInfo contact = ContactInfoMapper.toEntity(newContact);
        // check if the contact already exists
        if (checkContactExisted(newContact)) {
            logger.warning("Error: Contact already exists");
            throw new ContactAlreadyExistsException("This contact already exists.");
        }

        // check if the phone number already exists
        if (checkPhoneNumberExisted(newContact.getPhoneNumber())) {
            logger.warning("Error: Phone number already exists");
            throw new ContactAlreadyExistsException("Phone number already in use.");
        }
        
        logger.info("Success: Added new contact");
        MyContactDatabase.CONTACTS.add(contact);
        return;
    }


    @Override
    public void updateContactInfo(ContactInfoDTO selectedContactDTO, ContactInfoDTO updatedContactDTO)
    		throws ContactAlreadyExistsException, ContactNoneExistsException{
    	ContactInfo selectedContact = ContactInfoMapper.toEntity(selectedContactDTO);
    	ContactInfo updatedContact = ContactInfoMapper.toEntity(updatedContactDTO);
    	
    	// Map ánh xạ số điện thoại -> ContactInfo
    	Map<String , ContactInfo> contactMap = new HashMap<>();
    	
    	// khởi tạo map với list hiện tại 
    	for (ContactInfo c : MyContactDatabase.CONTACTS) {
    		contactMap.put(c.getPhoneNumber(), c);
    	}
    	
    	//check if selected contact's phone number existed?
    	if (!contactMap.containsKey(selectedContactDTO.getPhoneNumber())) {
    		logger.warning("Error: Selected contact doesn't exist");
    		throw new ContactNoneExistsException("Selected contact doesn't exist.");
    	}
    	
    	//check if phonenumber of updated contact ( new contact) existed in map and is not the same as the phone number of selected contact ( old contact  )
    	if (contactMap.containsKey(updatedContactDTO.getPhoneNumber()) &&
    			!updatedContactDTO.getPhoneNumber().equals(selectedContactDTO.getPhoneNumber())) {
    		logger.warning("Error: Phone number already exists");
    		throw new ContactAlreadyExistsException("Phone number already in use.");
    	}
    	
    	// if valid , then update
    	
    	contactMap.put(updatedContactDTO.getPhoneNumber(), updatedContact);
    	
    	// this right here has the wrong logic !!! must find exactly that contact, take it out and then repolace it with the updated one, not updating all list
    	MyContactDatabase.CONTACTS.remove(selectedContact); 
        MyContactDatabase.CONTACTS.add(updatedContact); 
    	 
    	 logger.info("Success: Updated contact info of " + selectedContactDTO.getFirstName() + " " + selectedContactDTO.getLastName());
    	 
    	 return;
    }

    @Override
    public Boolean checkContactExisted(ContactInfoDTO contactDTO) throws IllegalArgumentException {
        ContactInfo contact = ContactInfoMapper.toEntity(contactDTO);
        
        Map<String, ContactInfo > contactMap = new HashMap<>();
        
        for (ContactInfo c : MyContactDatabase.CONTACTS) {
    		contactMap.put(c.getPhoneNumber(), c);
    	}
        
        if (contactMap.containsKey(contactDTO.getPhoneNumber())) {
        	ContactInfo existedContact = contactMap.get(contactDTO.getPhoneNumber());
        	
        	if (existedContact.equals(contact)) {
        		logger.info("Checking: Contact exists");
                return true;
        	}
        }
        
        
        logger.info("Checking: Contact does not exist");
        return false;
    }

    @Override
    public Boolean checkPhoneNumberExisted(String phoneNumber) throws IllegalArgumentException {
		Map<String, ContactInfo> contactMap = new HashMap<>();

		for (ContactInfo c : MyContactDatabase.CONTACTS) {
			contactMap.put(c.getPhoneNumber(), c);
		}

		if (contactMap.containsKey(phoneNumber)) {
			logger.info("Checking: Phone number exists");
            return true;
		}

		 logger.info("Checking: Phone number does not exist");
         return false;
    }

    @Override
    public void deleteContacts(List<String> phoneNumbers) throws ContactNoneExistsException {
    	
        for (String phoneNumber : phoneNumbers) {
        	deleteContactByPhoneNumber(phoneNumber);
        }
        return;
    }
    
    @Override
    public Boolean deleteContactByFirstName(String firstName) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        for (ContactInfo contactInfo : MyContactDatabase.CONTACTS) {
            if (contactInfo.getFirstName().equalsIgnoreCase(firstName)) {
                MyContactDatabase.CONTACTS.remove(contactInfo);
                logger.info("Successfully deleted contact info of " + firstName);
                return true;
            }
        }
        logger.warning("Failed to delete contact info of " + firstName);
        return false;
    }

    @Override
    public void deleteContactByPhoneNumber(String phoneNumber) throws ContactNoneExistsException {

        // create map phone number -> ContactInfo
    	Map<String , ContactInfo> contactMap = new HashMap<>();
    	
    	// init map from list
    	for (ContactInfo c : MyContactDatabase.CONTACTS) {
    		contactMap.put(c.getPhoneNumber(), c);
    	}
    	
    	//check if phoneNumber existed
    	if (contactMap.containsKey(phoneNumber)) {
    		ContactInfo deletedContact = contactMap.remove(phoneNumber);
    		
    		// remove exactly deleted contact 
        	MyContactDatabase.CONTACTS.remove(deletedContact);  
            
            logger.info("Successfully deleted contact info for phone number " + phoneNumber);
            return ;
    	}
    	
    	// phone number not found 
    	logger.severe("Error: Phone number does not exist - " + phoneNumber);
        throw new ContactNoneExistsException("Selected phone number doesn't exist.");
    }

}

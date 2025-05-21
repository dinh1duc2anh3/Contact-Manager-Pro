package koolsoft.server;

import koolsoft.client.service.GreetingService;
import koolsoft.server.mapper.ContactInfoMapper;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.m;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private MyObjectifyDB myDB = new MyObjectifyDB();

    @Override
    public List<ContactInfo> getContactInfosByFirstName(String input) throws ContactNoneExistsException {
    	String lowerKeyword = input.toLowerCase().trim();
    	
    	return myDB.findByFirstName(lowerKeyword);
    }

    @Override
    public List<ContactInfo> getAllContactInfos() {
        return myDB.getAll();      
        
    }


    @Override
	public void addContactInfo(ContactInfo newContact) throws ContactAlreadyExistsException {
        myDB.add(newContact);
    	
        return;
    }


    @Override
    public void updateContactInfo(ContactInfo selectedContact, ContactInfo updatedContact)
    		throws ContactAlreadyExistsException, ContactNoneExistsException{
    	
    	myDB.update(selectedContact, updatedContact);
    	return;
    }



    @Override
    public void deleteContacts(List<String> phoneNumbers) throws ContactNoneExistsException {
        myDB.delete(phoneNumbers);
        return;
    }
    
    

    @Override
    public void deleteContactByPhoneNumber(String phoneNumber) throws ContactNoneExistsException {
        myDB.deleteByPhoneNumber(phoneNumber);
        return;
    }

}

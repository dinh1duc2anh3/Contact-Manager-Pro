package com.hello.server;

import com.hello.client.GreetingService;
import com.hello.shared.verifier.FieldVerifier;
import com.hello.client.GreetingService;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.formatter.ContactInfoFormatter;
import com.hello.shared.exception.ContactAlreadyExistsException; 
import com.hello.shared.exception.ContactNoneExistsException;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {


	private static final Logger log = Logger.getLogger(GreetingServiceImpl.class.getName());
	
	private MyObjectifyDB myDB = new MyObjectifyDB();

	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	
	@Override
    public ContactInfo getContactInfoById(Long id) throws ContactNoneExistsException  {
    	return myDB.findById(id);
    }
	
    @Override
    public List<ContactInfo> getContactInfosByFirstName(String firstName) throws ContactNoneExistsException  {
    	String formattedFirstName = ContactInfoFormatter.formatName(firstName);
    	return myDB.findByFirstName(formattedFirstName);
    }
    

	@Override
	public List<ContactInfo> getContactInfosByFullName(String fullName) throws ContactNoneExistsException {
		String formattedFullName = ContactInfoFormatter.formatName(fullName);
		return myDB.findByFullName(formattedFullName);
	}

	@Override
	public ContactInfo getContactInfosByPhoneNumber(String phoneNumber) throws ContactAlreadyExistsException{
		String formattedPhoneNumber = ContactInfoFormatter.formatPhoneNumber(phoneNumber);
		return myDB.findByPhoneNumber(formattedPhoneNumber);
	}
	
	@Override
	public List<ContactInfo> startsWithPhoneNumber(String phoneNumber){
		String formattedPhoneNumber = ContactInfoFormatter.formatPhoneNumber(phoneNumber);
		return myDB.startsWithPhoneNumber(formattedPhoneNumber);
	}

    @Override
    public List<ContactInfo> getAllContactInfos() {
        return myDB.findAll();      
    }


    @Override
	public void addContactInfo(ContactInfo newContact) throws ContactAlreadyExistsException {
		if (myDB.findByPhoneNumber(newContact.getPhoneNumber()) != null ) {
			log.warning("Error: Contact"+ newContact.getFirstName() + newContact.getLastName() + newContact.getPhoneNumber() + "already exists");
			throw new ContactAlreadyExistsException("This contact already exists.");
		}
    	myDB.add(newContact);
    	
        return;
    }


    
    @Override
    public void updateContactInfo(ContactInfo selectedContact, ContactInfo updatedContact) 
            throws ContactAlreadyExistsException, ContactNoneExistsException {
        System.out.println("GreetingServiceImpl: Calling update for phone: " + updatedContact.getPhoneNumber());
        myDB.update(selectedContact, updatedContact);
    }



    @Override
    public void deleteContacts(List<String> phoneNumbers) throws ContactNoneExistsException, ContactAlreadyExistsException {
        myDB.deleteByPhoneNumbers(phoneNumbers);
        return;
    }
    
    
    @Override
    public void deleteContactsByIds(List<Long> ids) throws ContactNoneExistsException {
        myDB.deleteByIds(ids);
    }
    
    @Override
    public void deleteContactById(Long id) throws ContactNoneExistsException {
        myDB.deleteById(id);
    }

    @Override
    public void deleteContactByPhoneNumber(String phoneNumber) throws ContactNoneExistsException, ContactAlreadyExistsException {
        myDB.deleteByPhoneNumber(phoneNumber);
        return;
    }
}

package koolsoft.server;

import koolsoft.client.service.GreetingService;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.ContactInfoFormatter;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

import java.util.List;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private MyObjectifyDB myDB = new MyObjectifyDB();

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
	public ContactInfo getContactInfosByPhoneNumber(String phoneNumber){
		String formattedPhoneNumber = ContactInfoFormatter.formatPhoneNumber(phoneNumber);
		return myDB.findByPhoneNumber(formattedPhoneNumber);
	}

    @Override
    public List<ContactInfo> getAllContactInfos() {
        return myDB.findAll();      
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

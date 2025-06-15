
package com.hello.client.activities.crud;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.ClientFactoryImpl;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.verifier.ContactInfoVerifier;
import com.hello.shared.enums.ActionType;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;

public class AddUpdateContactActivity {
	protected GreetingServiceAsync greetingService;

	protected MultiSelectionModel<ContactInfo> multiSelectionModel;
//	protected Set<ContactInfo> selectedContacts;
	protected ContactInfo selectedContact;
	private ActionType actionType;
	protected ListDataProvider<ContactInfo> dataProvider;
	
	private AddUpdateContactView view ;
	private ClientFactory clientFactory = new ClientFactoryImpl() ;


	public AddUpdateContactActivity(
			GreetingServiceAsync greetingService, 
			ListDataProvider<ContactInfo> dataProvider,
			MultiSelectionModel<ContactInfo> multiSelectionModel , 
			ActionType actionType, 
			SimplePanel overlay) {
		
		// Add null checks and logging
        if (multiSelectionModel == null) {
            GWT.log("ERROR: multiSelectionModel is null in constructor!");
            throw new IllegalArgumentException("multiSelectionModel cannot be null");
        }
        this.view = clientFactory.getAddUpdateContactView();
		this.greetingService = greetingService;
		this.multiSelectionModel = multiSelectionModel;
		Set<ContactInfo> selected = multiSelectionModel.getSelectedSet();
		this.selectedContact = selected.isEmpty() ? null : selected.iterator().next();
		
		this.actionType = actionType;
		this.dataProvider = dataProvider;
		this.view.setOverlay(overlay);
		
		view.initDialogStyle();
		bind();
	}

	private void bind() {
		view.getActionButton().addClickHandler(event -> onActionButtonClick());
		view.getCloseButton().addClickHandler(event -> closeDialog());
	}

	private void onActionButtonClick() {

		String fn = view.getFirstNameBox().getText().trim();
		String ln = view.getLastNameBox().getText().trim();
		String pn = view.getPhoneNumberBox().getText().trim();
		String ad = view.getAddressBox().getText().trim();

		// validate input
		if (!ContactInfoVerifier.contactInfoVerifier(fn, ln, pn, ad)) {
			GWT.log("Error: validate contact info ");
			return;
		} else
			GWT.log("Success: validate contact info ");

		ContactInfo contactInfo = new ContactInfo(fn, ln, pn, ad);

		// Add or update logic
		if (ActionType.ADD.equals(actionType)) {
			addContactInfo(contactInfo);
		} else if (ActionType.UPDATE.equals(actionType)) {
			if (selectedContact == null) {
				GWT.log("Error: selected contact is null ");
				return;
			}
			updateContactInfo(selectedContact, contactInfo);
		}
		closeDialog();
	}

	// Add contact
	private void addContactInfo(ContactInfo newContactInfo) {
		greetingService.addContactInfo(newContactInfo, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof ContactAlreadyExistsException) {
					Window.alert("Error: " + caught.getMessage());
				} else {
					GWT.log("Error: unexpected error", caught);
					Window.alert("An unexpected error occurred while adding the contact.");
				}
			}

			public void onSuccess(Void result) {
				GWT.log("AddUpdateContactActivity: Successfully add contact"+ newContactInfo.getFullName());
				Window.alert("Successfully add contact"+newContactInfo.getFullName());
				
				dataProvider.getList().add(newContactInfo);
				dataProvider.refresh();
				
				ContactInfoCache.addContact(newContactInfo);
				
				
				closeDialog();
			}
		});
	}

	// Update contact
	private void updateContactInfo(
			ContactInfo selectedContact,
			ContactInfo updatedContact) {
		greetingService.updateContactInfo(selectedContact, updatedContact, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Caught exception class: " + caught.getClass().getName());
				if (caught instanceof ContactAlreadyExistsException) {
					Window.alert("Error: " + caught.getMessage());
				} else if (caught instanceof ContactNoneExistsException) {
					Window.alert("Error: " + caught.getMessage());
				} else {
					GWT.log("Unexpected error", caught);
					Window.alert("An unexpected error occurred while updating the contact.");
				}
			}

			@Override
			public void onSuccess(Void result) {
				GWT.log("AddUpdateContactActivity: Successfully update contact"+updatedContact.getFullName());
				Window.alert("Successfully update contact"+updatedContact.getFullName());
				
				dataProvider.getList().remove(selectedContact);
				dataProvider.getList().add(updatedContact);
				dataProvider.refresh();
				ContactInfoCache.removeContact(selectedContact);
				ContactInfoCache.addContact(updatedContact);
				
				closeDialog();
			}
		});
	}

	
	
	void closeDialog() {
		try {
			// Clear selection AFTER everything else is done
			// Use a deferred command to avoid interference with dialog closing
			com.google.gwt.core.client.Scheduler.get()
					.scheduleDeferred(new com.google.gwt.core.client.Scheduler.ScheduledCommand() {
						@Override
						public void execute() {
							try {
								if (multiSelectionModel != null) {
									multiSelectionModel.clear();
								}
								selectedContact = null;
								GWT.log("Selection cleared successfully");
							} catch (Exception e) {
								GWT.log("Error clearing selection: " + e.getMessage(), e);
							}
						}
					});
			view.closeDialogBox();

			GWT.log("closeDialog() method completed");

		} catch (Exception e) {
	        GWT.log("Error in closeDialog(): " + e.getMessage(), e);
	    }
	}
	
	public void showDialog() {
		view.showDialogBox(selectedContact); 
	};
}

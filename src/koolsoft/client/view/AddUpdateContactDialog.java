
package koolsoft.client.view;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.client.util.OverlayUtil;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.ContactInfoVerifier;
import koolsoft.shared.enums.ActionType;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

public class AddUpdateContactDialog {
	protected GreetingServiceAsync greetingService;

	protected Button triggerButton;

	protected MultiSelectionModel<ContactInfo> multiSelectionModel;

	protected Set<ContactInfo> selectedContacts;
	protected ContactInfo selectedContact;

	private ActionType actionType;
	private SimplePanel overlay;
	
	@UiField
	DialogBox dialogBox;
	@UiField
	TextBox firstNameBox;
	@UiField
	TextBox lastNameBox;
	@UiField
	TextBox phoneNumberBox;
	@UiField
	TextBox addressBox;
	
	@UiField
	Button actionButton; // "Add" hoặc "Update"
	@UiField
	Button closeButton;// button mở dialog ("Add Contact" hoặc "Update Contact")

	protected ListDataProvider<ContactInfo> dataProvider;

	interface AddUpdateContactInfoDialogUiBinder extends UiBinder<Widget, AddUpdateContactDialog> {
	}

	private static AddUpdateContactInfoDialogUiBinder uiBinder = GWT.create(AddUpdateContactInfoDialogUiBinder.class);

	public AddUpdateContactDialog(GreetingServiceAsync greetingService, Button triggerButton,
			ListDataProvider<ContactInfo> dataProvider,
			MultiSelectionModel<ContactInfo> multiSelectionModel , ActionType actionType, SimplePanel overlay) {
		
		uiBinder.createAndBindUi(this);
		
		// Add null checks and logging
        if (multiSelectionModel == null) {
            GWT.log("ERROR: multiSelectionModel is null in constructor!");
            throw new IllegalArgumentException("multiSelectionModel cannot be null");
        }
        
		this.greetingService = greetingService;
		this.triggerButton = triggerButton;
		this.dataProvider = dataProvider;
		Set<ContactInfo> selected = multiSelectionModel.getSelectedSet();
		this.selectedContact = selected.isEmpty() ? null : selected.iterator().next();
		this.actionType = actionType;
		this.overlay = overlay;
		
		// Đảm bảo DialogBox có đúng style
        dialogBox.addAttachHandler(event -> {
            if (event.isAttached()) {
                Element dialogElement = dialogBox.getElement();
                dialogElement.getStyle().setWidth(400, Unit.PX);
//                dialogElement.getStyle().setMaxWidth(400, Unit.PX);
                dialogElement.getStyle().setProperty("boxSizing", "border-box");
            }
        });
	}

	@UiHandler("actionButton")
	protected void onActionButtonClick(ClickEvent event) {
		String fn = firstNameBox.getText().trim();
		String ln = lastNameBox.getText().trim();
		String pn = phoneNumberBox.getText().trim();
		String ad = addressBox.getText().trim();

		// validate input
		if (!ContactInfoVerifier.contactInfoVerifier(fn, ln, pn, ad)) {
			GWT.log("Error: validate contact info ");
			return;
		} else
			GWT.log("Success: validate contact info ");

		ContactInfo contactInfo = new ContactInfo(fn, ln, pn, ad);

		// Add or update logic
		if (actionType.equals(ActionType.ADD)) {
			addContactInfo(contactInfo);
		} else if (actionType.equals(ActionType.UPDATE)) {
			updateContactInfo(contactInfo);
		}
		closeDialog();
	};

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
				GWT.log("Success: adding contact info");
				dataProvider.getList().add(newContactInfo);
				dataProvider.refresh();
				closeDialog();
			}
		});
	}

	// Update contact
	private void updateContactInfo(ContactInfo updatedContact) {
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
				dataProvider.getList().remove(selectedContact);
				dataProvider.getList().add(updatedContact);
				dataProvider.refresh();
				closeDialog();
			}
		});
	}

	@UiHandler("closeButton")
	void closeDialogEvent(ClickEvent event) {
		closeDialog();
	}
	
	void closeDialog() {
		try {
	        
	        // Clear fields first
	        clearFields();
	        
	        // Hide dialog and remove overlay
	        dialogBox.hide();
	        OverlayUtil.removeOverlay(overlay);
	        
	        // Re-enable trigger button
	        if (triggerButton != null) {
	            triggerButton.setEnabled(true);
	        }
	        
	        // Clear selection AFTER everything else is done
	        // Use a deferred command to avoid interference with dialog closing
	        com.google.gwt.core.client.Scheduler.get().scheduleDeferred(new com.google.gwt.core.client.Scheduler.ScheduledCommand() {
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
	        
	        GWT.log("closeDialog() method completed");
	        
	    } catch (Exception e) {
	        GWT.log("Error in closeDialog(): " + e.getMessage(), e);
	    }
	}

	protected void clearFields() {
		firstNameBox.setText("");
		lastNameBox.setText("");
		phoneNumberBox.setText("");
		addressBox.setText("");
	}

	public void showDialog() {

		OverlayUtil.displayOverlay(overlay);

		if (selectedContact == null) {
			actionButton.setText("Add");
			clearFields();
		} else {
			actionButton.setText("Update");
			firstNameBox.setText(selectedContact.getFirstName());
			lastNameBox.setText(selectedContact.getLastName());
			phoneNumberBox.setText(selectedContact.getPhoneNumber());
			addressBox.setText(selectedContact.getAddress());
		}

		dialogBox.center();
		dialogBox.show();
		triggerButton.setEnabled(false);
	};
}

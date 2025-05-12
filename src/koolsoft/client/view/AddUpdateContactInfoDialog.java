
package koolsoft.client.view;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import koolsoft.client.enums.ActionType;
import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.shared.ContactInfoDTO;
import koolsoft.shared.ContactInfoVerifier;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

public class AddUpdateContactInfoDialog {
	protected GreetingServiceAsync greetingService = null;

	protected Button triggerButton;

	protected MultiSelectionModel<ContactInfoDTO> multiSelectionModel = null;

	protected Set<ContactInfoDTO> selectedContacts = null;
	protected ContactInfoDTO selectedContact = null;

	private ActionType actionType;
	
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

	protected ListDataProvider<ContactInfoDTO> dataProvider;

	interface AddUpdateContactInfoDialogUiBinder extends UiBinder<Widget, AddUpdateContactInfoDialog> {
	}

	private static AddUpdateContactInfoDialogUiBinder uiBinder = GWT.create(AddUpdateContactInfoDialogUiBinder.class);

	public AddUpdateContactInfoDialog(GreetingServiceAsync greetingService, Button triggerButton,
			ListDataProvider<ContactInfoDTO> dataProvider, ContactInfoDTO selectedContact,
			MultiSelectionModel<ContactInfoDTO> multiSelectionModel , ActionType actionType) {
		uiBinder.createAndBindUi(this);
		this.greetingService = greetingService;
		this.triggerButton = triggerButton;
		this.dataProvider = dataProvider;
		this.selectedContact = selectedContact;
		this.multiSelectionModel = multiSelectionModel;
		this.actionType = actionType;
	}

	@UiHandler("actionButton")
	protected void onActionButtonClick(ClickEvent event) {
		String fn = firstNameBox.getText();
		String ln = lastNameBox.getText();
		String pn = phoneNumberBox.getText();
		String ad = addressBox.getText();

		// validate input
		if (!ContactInfoVerifier.contactInfoVerifier(fn, ln, pn, ad)) {
			GWT.log("Error: validate contact info ");
			return;
		} else
			GWT.log("Success: validate contact info ");

		ContactInfoDTO contactInfo = new ContactInfoDTO(fn, ln, pn, ad);

		// Add or update logic
		if (actionType.equals(ActionType.ADD)) {
			addContactInfo(contactInfo);
		} else if (actionType.equals(ActionType.UPDATE)) {
			updateContactInfo(contactInfo);
		}
	};

	// Add contact
	private void addContactInfo(ContactInfoDTO newContactInfo) {
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
	private void updateContactInfo(ContactInfoDTO updatedContact) {
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
		dialogBox.hide();
		clearFields();
		triggerButton.setEnabled(true);
		selectedContact = null;
		multiSelectionModel.clear();
	};

	protected void clearFields() {
		firstNameBox.setText("");
		lastNameBox.setText("");
		phoneNumberBox.setText("");
		addressBox.setText("");
	}

	public void showDialog() {
		dialogBox.center();

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

		dialogBox.show();
		triggerButton.setEnabled(false);
	};
}

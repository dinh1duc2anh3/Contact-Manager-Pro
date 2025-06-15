package com.hello.client.activities.crud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.client.activities.basic.BasicViewImpl;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.utils.OverlayUtil;

public class AddUpdateContactViewImpl extends BasicViewImpl implements AddUpdateContactView {
	private static AddUpdateContactViewImplUiBinder uiBinder = GWT.create(AddUpdateContactViewImplUiBinder.class);

	interface AddUpdateContactViewImplUiBinder extends UiBinder<Widget, AddUpdateContactViewImpl> {
	}
	
	public AddUpdateContactViewImpl() {
		this.layout.getContainerPanel().add((uiBinder.createAndBindUi(this)));
	}
	
	private SimplePanel overlay;
	@UiField DialogBox dialogBox;
	@UiField TextBox firstNameBox;
	@UiField TextBox lastNameBox;
	@UiField TextBox phoneNumberBox;
	@UiField TextBox addressBox;
	@UiField Button actionButton; // "Add" hoặc "Update"
	@UiField Button closeButton;// button mở dialog ("Add Contact" hoặc "Update Contact")
	
	@Override
	public SimplePanel getOverlay() {
		return overlay;
	}
	
	@Override
	public void setOverlay(SimplePanel overlay) {
		this.overlay = overlay;
	}

	@Override
	public DialogBox getDialogBox() {
		return dialogBox;
	}

	@Override
	public TextBox getFirstNameBox() {
		return firstNameBox;
	}

	@Override
	public TextBox getLastNameBox() {
		return lastNameBox;
	}

	@Override
	public TextBox getPhoneNumberBox() {
		return phoneNumberBox;
	}

	@Override
	public TextBox getAddressBox() {
		return addressBox;
	}

	@Override
	public Button getActionButton() {
		return actionButton;
	}

	@Override
	public Button getCloseButton() {
		return closeButton;
	}
	
	@Override
	public void initDialogStyle() {
	    dialogBox.addAttachHandler(event -> {
	        if (event.isAttached()) {
	            Element dialogElement = dialogBox.getElement();
	            dialogElement.getStyle().setWidth(400, Unit.PX);
	            dialogElement.getStyle().setProperty("boxSizing", "border-box");
	        }
	    });
	}


	@Override
	public void closeDialogBox() {
		clearFields();
		dialogBox.hide();
		OverlayUtil.removeOverlay(overlay);

		// Re-enable trigger button
		if (actionButton != null) {
			actionButton.setEnabled(true);
		}
	}
	

	@Override
	public void showDialogBox(ContactInfo selectedContact) {
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
		
		actionButton.setEnabled(true);
	}

	@Override
	public void clearFields() {
		firstNameBox.setText("");
		lastNameBox.setText("");
		phoneNumberBox.setText("");
		addressBox.setText("");
		
	}

	

}

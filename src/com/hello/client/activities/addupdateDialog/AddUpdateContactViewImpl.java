package com.hello.client.activities.addupdateDialog;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.client.activities.basic.BasicViewImpl;
import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.utils.OverlayUtil;

public class AddUpdateContactViewImpl extends BasicViewImpl implements AddUpdateContactView {
	private static AddUpdateContactViewImplUiBinder uiBinder = GWT.create(AddUpdateContactViewImplUiBinder.class);

	interface AddUpdateContactViewImplUiBinder extends UiBinder<Widget, AddUpdateContactViewImpl> {
	}
	
	
	
	private SimplePanel overlay;
	@UiField DialogBox dialogBox;
	@UiField TextBox firstNameBox;
	@UiField TextBox lastNameBox;
	@UiField TextBox phoneNumberBox;
//	@UiField ListBox addressListBox;
	@UiField(provided = true) ValueListBox<Address> addressValueListBox;
	@UiField(provided = true) ValueListBox<Gender> genderValueListBox;
	@UiField Button actionButton; // "Add" hoặc "Update"
	@UiField Button closeButton;// button mở dialog ("Add Contact" hoặc "Update Contact")
	
	
	public AddUpdateContactViewImpl() {
		addressValueListBox = new ValueListBox<Address>(new AbstractRenderer<Address>() {
	        @Override
	        public String render(Address address) {
	            return address == null ? "Chọn địa chỉ" : address.toString(); // hoặc .getDisplayName()
	        }
	    });
		
		genderValueListBox = new ValueListBox<Gender>(new AbstractRenderer<Gender>() {
	        @Override
	        public String render(Gender gender) {
	            return gender == null ? "Chọn giới tính" : gender.toString(); // hoặc .getDisplayName()
	        }
	    });
		
		this.layout.getContainerPanel().add((uiBinder.createAndBindUi(this)));
	}
	
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
	public ValueListBox<Address> getAddressValueListBox() {
		return addressValueListBox;
	}
	
	@Override
	public ValueListBox<Gender> getGenderValueListBox() {
		return genderValueListBox;
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
		addressValueListBox.setValue(null);
	    addressValueListBox.setAcceptableValues(Arrays.asList(Address.values()));
	    
	    genderValueListBox.setValue(null);
	    genderValueListBox.setAcceptableValues(Arrays.asList(Gender.values()));
		
		
		
		if (selectedContact == null) {
			actionButton.setText("Add");
			clearFields();
		} else {
			actionButton.setText("Update");
			firstNameBox.setText(selectedContact.getFirstName());
			lastNameBox.setText(selectedContact.getLastName());
			genderValueListBox.setValue(selectedContact.getGender());
			phoneNumberBox.setText(selectedContact.getPhoneNumber());
			addressValueListBox.setValue(selectedContact.getAddress());
			
		}
		dialogBox.center();
		dialogBox.show();
		
		actionButton.setEnabled(true);
	}

	@Override
	public void clearFields() {
		firstNameBox.setText("");
		lastNameBox.setText("");
		genderValueListBox.setValue(null);
		phoneNumberBox.setText("");
		addressValueListBox.setValue(null);
	}

	

}

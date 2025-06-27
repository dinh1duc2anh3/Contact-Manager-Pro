package com.hello.client.activities.addupdate2;

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

public class AddUpdateContactViewImpl2 extends BasicViewImpl implements AddUpdateContactView2 {
	private static AddUpdateContactViewImplUiBinder uiBinder = GWT.create(AddUpdateContactViewImplUiBinder.class);

	interface AddUpdateContactViewImplUiBinder extends UiBinder<Widget, AddUpdateContactViewImpl2> {
	}
	
	@UiField TextBox firstNameBox;
	@UiField TextBox lastNameBox;
	@UiField TextBox phoneNumberBox;
	@UiField(provided = true) ValueListBox<Address> addressValueListBox;
	@UiField(provided = true) ValueListBox<Gender> genderValueListBox;
	@UiField Button actionButton;
	@UiField Button closeButton;
	
	
	public AddUpdateContactViewImpl2() {
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
	
	@Override public TextBox getFirstNameBox() { return firstNameBox; }
	@Override public TextBox getLastNameBox() { return lastNameBox; }
	@Override public TextBox getPhoneNumberBox() { return phoneNumberBox; }
	@Override public ValueListBox<Address> getAddressValueListBox() { return addressValueListBox; }
	@Override public ValueListBox<Gender> getGenderValueListBox() { return genderValueListBox; }
	@Override public Button getActionButton() { return actionButton; }
	@Override public Button getCloseButton() { return closeButton; }
	
	
	@Override
	public void showForm(ContactInfo contact) {
		addressValueListBox.setAcceptableValues(Arrays.asList(Address.values()));
		genderValueListBox.setAcceptableValues(Arrays.asList(Gender.values()));
		if (contact == null) {
			actionButton.setText("Add");
			clearFields();
		} else {
			actionButton.setText("Update");
			firstNameBox.setText(contact.getFirstName());
			lastNameBox.setText(contact.getLastName());
			phoneNumberBox.setText(contact.getPhoneNumber());
			addressValueListBox.setValue(contact.getAddress());
			genderValueListBox.setValue(contact.getGender());
		}
	}
	@Override
	public void clearFields() {
		firstNameBox.setText("");
		lastNameBox.setText("");
		phoneNumberBox.setText("");
		genderValueListBox.setValue(null);
		addressValueListBox.setValue(null);
	}

	

}

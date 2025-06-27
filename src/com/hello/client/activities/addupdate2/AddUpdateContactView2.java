package com.hello.client.activities.addupdate2;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.client.activities.basic.BasicView;
import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.model.ContactInfo;

public interface AddUpdateContactView2 extends BasicView  {
	TextBox getFirstNameBox();
	TextBox getLastNameBox();
	TextBox getPhoneNumberBox();
	ValueListBox<Gender> getGenderValueListBox();
	ValueListBox<Address> getAddressValueListBox();
	Button getActionButton();
	Button getCloseButton();
	void showForm(ContactInfo contact); 
	void clearFields();
}

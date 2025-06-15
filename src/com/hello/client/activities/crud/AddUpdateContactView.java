package com.hello.client.activities.crud;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.shared.model.ContactInfo;

public interface AddUpdateContactView {
	SimplePanel getOverlay();
	void setOverlay(SimplePanel overlay);
	DialogBox getDialogBox();
	TextBox getFirstNameBox();
	TextBox getLastNameBox();
	TextBox getPhoneNumberBox();
	TextBox getAddressBox();
	Button getActionButton(); 
	Button getCloseButton();
	void showDialogBox(ContactInfo selectedContact);
	void clearFields();
	void closeDialogBox();
	void initDialogStyle();
}

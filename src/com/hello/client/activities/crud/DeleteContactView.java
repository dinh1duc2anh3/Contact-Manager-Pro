package com.hello.client.activities.crud;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.hello.shared.model.ContactInfo;

public interface DeleteContactView {

	void setOverlay(SimplePanel overlay);

	SimplePanel getOverlay();

	DialogBox getDialogBox();

	Button getCloseButton();

	Button getDeleteButton();

	Label getDeleteConfirmLabel();

	void showDialogBox();

	void closeDialogBox();

	void initDialogStyle();
	
}

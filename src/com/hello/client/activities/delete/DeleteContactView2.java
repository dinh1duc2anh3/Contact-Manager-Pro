package com.hello.client.activities.delete;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.hello.shared.model.ContactInfo;

public interface DeleteContactView2 extends IsWidget {
    Label getConfirmLabel();
    Button getYesButton();
    Button getNoButton();
}

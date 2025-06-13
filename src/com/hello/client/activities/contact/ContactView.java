package com.hello.client.activities.contact;

import org.gwtbootstrap3.client.ui.Button;

import com.hello.client.activities.basic.BasicView;
import com.hello.shared.model.User;

public interface ContactView extends BasicView {

	void showContact(User user);

	Button getBtnBack();

	Button getBtnEvent();

}

package com.hello.client.activities.home;

import org.gwtbootstrap3.client.ui.Button;

import com.hello.client.activities.basic.BasicView;
import com.hello.shared.model.User;

public interface HomeView extends BasicView {

	Button getBtnSend();

	User getUser();

}

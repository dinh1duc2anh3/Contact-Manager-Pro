package com.hello.client.activities.home;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.hello.client.activities.basic.BasicViewImpl;
import com.hello.shared.model.User;

public class HomeViewImpl extends BasicViewImpl implements HomeView  {

	private static HomeViewImplUiBinder uiBinder = GWT.create(HomeViewImplUiBinder.class);

	interface HomeViewImplUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}
	
	@UiField
	TextBox tbName, tbAdress;
	@UiField Button btnSend;
	
	public HomeViewImpl() {
		this.layout.getContainerPanel().add(uiBinder.createAndBindUi(this));
		tbName.setPlaceholder("Nhập tên");
		tbAdress.setPlaceholder("Nhập địa chỉ");
	}
	
	@Override
	public User getUser() {
		String name = tbName.getText().trim();
		String address = tbAdress.getText().trim();
		if(name.isEmpty()) {
			Window.alert("Vui lòng nhập tên");
			return null;
		}
		if(address.isEmpty()) {
			Window.alert("Vui lòng nhập địa chỉ");
			return null;
		}
		return new User(name, address);
	}
	
	@Override
	public Button getBtnSend() {
		return btnSend;
	}
	
}

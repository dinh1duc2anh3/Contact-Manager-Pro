package com.hello.client.activities.home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.hello.client.ClientUtils;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.basic.BasicActivity;
import com.hello.client.activities.contact.ContactPlace;
import com.hello.shared.model.User;

public class HomeActivity extends BasicActivity {

	private HomeView view;
	
	public HomeActivity(ClientFactory clientFactory, Place place) {
		super(clientFactory, place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getHomeView();
		ClientUtils.log("setWidget");
		panel.setWidget(view.asWidget());
		ClientUtils.log("startxxx");
		super.start(panel, eventBus, view);
		ClientUtils.log("start ok");
	}
	
	@Override
	protected void bind() {
		super.bind();
		addHandlerRegistration(view.getBtnSend().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				User user = view.getUser();
				if(user == null) {
					return;
				}
				clientFactory.getPlaceController().goTo(new ContactPlace(user));
			}
		}));
	}
}

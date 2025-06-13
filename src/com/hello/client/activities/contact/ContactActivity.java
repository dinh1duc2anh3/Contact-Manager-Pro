package com.hello.client.activities.contact;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.basic.BasicActivity;
import com.hello.client.activities.home.HomePlace;
import com.hello.client.events.ActionEvent;
import com.hello.client.events.ActionEvent.Action;
import com.hello.shared.model.User;

public class ContactActivity extends BasicActivity {

	private ContactView view;
	private User user;
	
	public ContactActivity(ClientFactory clientFactory, Place place) {
		super(clientFactory, place);
		user = ((ContactPlace)place).getUser();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getContactView();
		panel.setWidget(view.asWidget());
		super.start(panel, eventBus, view);
	}
	
	@Override
	public void loadData() {
		view.showContact(user);
	}
	
	@Override
	protected void bind() {
		super.bind();
		addHandlerRegistration(view.getBtnBack().addClickHandler(new  ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clientFactory.getPlaceController().goTo(new HomePlace());
			}
		}));
		
		addHandlerRegistration(view.getBtnEvent().addClickHandler(new  ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ActionEvent(Action.CLICK, user));
			}
		}));
	}
}

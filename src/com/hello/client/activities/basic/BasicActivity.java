package com.hello.client.activities.basic;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.hello.client.AppManager;
import com.hello.client.activities.ClientFactory;
import com.hello.client.events.ActionEvent;
import com.hello.client.events.ActionEventHandler;

public class BasicActivity extends MGWTAbstractActivity {
	
	protected ClientFactory clientFactory;
	protected EventBus eventBus;
	protected Place place;
	
	public BasicActivity(ClientFactory clientFactory, Place place) {
		super();
		this.clientFactory = clientFactory;
		this.place = place;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void start(AcceptsOneWidget panel, EventBus eventBus, final BasicView basicView) {
		this.eventBus = eventBus;
		bind();
		loadData();
	}

	protected void bind() {
		addHandlerRegistration(eventBus.addHandler(ActionEvent.TYPE, new ActionEventHandler() {
			@Override
			public void onEvent(ActionEvent event) {
				Window.alert("Event : "  + event.getUser().getName());
			}
		}));
	}
	
	public void loadData() {
		
	}
	
	protected void refreshView() {
		
	}
	
	protected void gotoPage(Place newPlace) {
		AppManager.CLIENT_FACTORY.getPlaceController().goTo(newPlace);
	}
}
package com.hello.client.activities;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.hello.client.GreetingService;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.contact.ContactView;
import com.hello.client.activities.home.HomeView;
import com.hello.client.activities.homepage.HomepageView;

public interface ClientFactory {
	
	public PlaceController getPlaceController();
	public GreetingServiceAsync getGreetingService();
	public EventBus getEventBus();
	public HomeView getHomeView();
	public HomepageView getHomepageView();
	public ContactView getContactView();

}

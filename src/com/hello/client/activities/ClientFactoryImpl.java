package com.hello.client.activities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.hello.client.GreetingService;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.contact.ContactView;
import com.hello.client.activities.contact.ContactViewImpl;
import com.hello.client.activities.crud.AddUpdateContactView;
import com.hello.client.activities.crud.AddUpdateContactViewImpl;
import com.hello.client.activities.crud.DeleteContactView;
import com.hello.client.activities.crud.DeleteContactViewImpl;
import com.hello.client.activities.home.HomeView;
import com.hello.client.activities.home.HomeViewImpl;
import com.hello.client.activities.homepage.HomepageView;
import com.hello.client.activities.homepage.HomepageViewImpl;

public class ClientFactoryImpl implements ClientFactory {
	
//	protected SimpleEventBus eventBus;
//	protected PlaceController placeController;
//	protected GreetingServiceAsync greetingService;
//	private HomeView homeView;
//	private HomepageView homepageView;
//	private ContactView contactView;
	
	private final EventBus eventBus = new SimpleEventBus();
    private final PlaceController placeController = new PlaceController(eventBus);
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
    private final HomepageView homepageView = new HomepageViewImpl();
    private final HomeView homeView = new HomeViewImpl(); // From your example
    private final ContactView contactView = new ContactViewImpl();
    private final AddUpdateContactView addUpdateContactView = new AddUpdateContactViewImpl();
    private final DeleteContactView deleteContactView = new DeleteContactViewImpl();
	
    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public GreetingServiceAsync getGreetingService() {
        return greetingService;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public HomeView getHomeView() {
        return homeView;
    }

    @Override
    public HomepageView getHomepageView() {
        return homepageView;
    }

    @Override
    public ContactView getContactView() {
        return contactView;
    }

	@Override
	public AddUpdateContactView getAddUpdateContactView() {
		return addUpdateContactView;
	}

	@Override
	public DeleteContactView getDeleteContactView() {
		return deleteContactView;
	}

}

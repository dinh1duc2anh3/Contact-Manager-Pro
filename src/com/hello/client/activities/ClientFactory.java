package com.hello.client.activities;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.EventBus;
import com.hello.client.GreetingService;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.addupdate2.AddUpdateContactView2;
import com.hello.client.activities.addupdateDialog.AddUpdateContactView;
import com.hello.client.activities.contact.ContactView;
import com.hello.client.activities.delete.DeleteContactView2;
import com.hello.client.activities.deleteDialog.DeleteContactView;
import com.hello.client.activities.home.HomeView;
import com.hello.client.activities.homepage.HomepageActivity;
import com.hello.client.activities.homepage.HomepageView;
import com.hello.shared.model.ContactInfo;

public interface ClientFactory {
	
	public PlaceController getPlaceController();
	public GreetingServiceAsync getGreetingService();
	public ListDataProvider<ContactInfo> getDataProvider();
	public EventBus getEventBus();
	public HomeView getHomeView();
	public HomepageView getHomepageView();
	public ContactView getContactView();
	public AddUpdateContactView getAddUpdateContactView();
	public DeleteContactView getDeleteContactView(); //dialog
	public DeleteContactView2 getDeleteContactView2(); // screen
	public AddUpdateContactView2 getAddUpdateContactView2();
	public HomepageActivity getHomepageActivity();
	public void setHomePageActivity(HomepageActivity activity);
}

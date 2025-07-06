package com.hello.client.activities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.hello.client.GreetingService;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.addupdate2.AddUpdateContactActivity2;
import com.hello.client.activities.addupdate2.AddUpdateContactView2;
import com.hello.client.activities.addupdate2.AddUpdateContactViewImpl2;
import com.hello.client.activities.addupdateDialog.AddUpdateContactView;
import com.hello.client.activities.addupdateDialog.AddUpdateContactViewImpl;
import com.hello.client.activities.contact.ContactView;
import com.hello.client.activities.contact.ContactViewImpl;
import com.hello.client.activities.delete.DeleteContactActivity2;
import com.hello.client.activities.delete.DeleteContactView2;
import com.hello.client.activities.delete.DeleteContactViewImpl2;
import com.hello.client.activities.deleteDialog.DeleteContactView;
import com.hello.client.activities.deleteDialog.DeleteContactViewImpl;
import com.hello.client.activities.home.HomeView;
import com.hello.client.activities.home.HomeViewImpl;
import com.hello.client.activities.homepage.HomepageActivity;
import com.hello.client.activities.homepage.HomepageView;
import com.hello.client.activities.homepage.HomepageViewImpl;
import com.hello.shared.model.ContactInfo;

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
    private final ListDataProvider<ContactInfo> dataProvider = new ListDataProvider<>();
    
    
    private final HomepageView homepageView = new HomepageViewImpl(eventBus);
    private final AddUpdateContactView addUpdateContactView = new AddUpdateContactViewImpl();
    
    private final AddUpdateContactView2 addUpdateContactView2 = new AddUpdateContactViewImpl2();
    private final DeleteContactView2 deleteContactView2 = new DeleteContactViewImpl2();
    private final DeleteContactView deleteContactView = new DeleteContactViewImpl();
    private final HomeView homeView = new HomeViewImpl(); // From your example
    private final ContactView contactView = new ContactViewImpl();
    
    private HomepageActivity homepageActivity;
    private AddUpdateContactActivity2 addUpdateContactActivity2;
    private DeleteContactActivity2 deleteContactActivity2;
	
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

	@Override
	public DeleteContactView2 getDeleteContactView2() {
		return deleteContactView2;
	}

	@Override
	public AddUpdateContactView2 getAddUpdateContactView2() {
		if (addUpdateContactView2 == null) {
            GWT.log("ClientFactoryImpl - AddUpdateContactViewImpl2 is null , cant get view");
        }
        return addUpdateContactView2;
	}

	@Override
	public ListDataProvider<ContactInfo> getDataProvider() {
		return dataProvider;
	}

	@Override
    public HomepageActivity getHomepageActivity() {
        return homepageActivity;
    }
	
	@Override
    public void setHomePageActivity(HomepageActivity activity) {
        this.homepageActivity = activity;
    }
	
	@Override
    public AddUpdateContactActivity2 getAddUpdateContactActivity2() {
        return addUpdateContactActivity2;
    }

    @Override
    public void setAddUpdateContactActivity2(AddUpdateContactActivity2 activity) {
        this.addUpdateContactActivity2 = activity;
    }

    @Override
    public DeleteContactActivity2 getDeleteContactActivity2() {
        return deleteContactActivity2;
    }

    @Override
    public void setDeleteContactActivity2(DeleteContactActivity2 activity) {
        this.deleteContactActivity2 = activity;
    }

}

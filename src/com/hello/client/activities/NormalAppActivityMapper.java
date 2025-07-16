package com.hello.client.activities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.hello.client.activities.addupdate2.AddUpdateContactActivity2;
import com.hello.client.activities.addupdate2.AddUpdateContactPlace2;
import com.hello.client.activities.contact.ContactActivity;
import com.hello.client.activities.contact.ContactPlace;
import com.hello.client.activities.delete.DeleteContactActivity2;
import com.hello.client.activities.delete.DeleteContactPlace2;
import com.hello.client.activities.home.HomeActivity;
import com.hello.client.activities.home.HomePlace;
import com.hello.client.activities.homepage.HomepageActivity;
import com.hello.client.activities.homepage.HomepagePlace;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.enums.ActionType;


public class NormalAppActivityMapper implements AsyncActivityMapper {
	
	private ClientFactory clientFactory ;
	
	public NormalAppActivityMapper(ClientFactory clientFactory){
		this.clientFactory = clientFactory;
	}

	@Override
	public void getActivity(final Place place, final ActivityCallbackHandler activityCallbackHandler) {
		
		if (place instanceof HomePlace) {
			activityCallbackHandler.onRecieveActivity( new HomeActivity(clientFactory, place));
		} 
		else if (place instanceof ContactPlace) {
			activityCallbackHandler.onRecieveActivity( new ContactActivity(clientFactory, place));
		} 
		else if (place instanceof HomepagePlace) {
			HomepageActivity homepageActivity = clientFactory.getHomepageActivity();
		    activityCallbackHandler.onRecieveActivity(homepageActivity);
		}
		else if (place instanceof AddUpdateContactPlace2) {
		    AddUpdateContactActivity2 addUpdateContactActivity2 =  new AddUpdateContactActivity2(clientFactory, place);
		    activityCallbackHandler.onRecieveActivity(addUpdateContactActivity2);
        }
		else if (place instanceof DeleteContactPlace2) {
            DeleteContactActivity2 deleteActivity = new DeleteContactActivity2(clientFactory, place);
            activityCallbackHandler.onRecieveActivity(deleteActivity);
        }
	}
}

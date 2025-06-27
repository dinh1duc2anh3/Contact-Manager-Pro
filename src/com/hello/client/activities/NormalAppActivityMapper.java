package com.hello.client.activities;

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


public class NormalAppActivityMapper implements AsyncActivityMapper {
	
	private ClientFactory clientFactory;
	
	public NormalAppActivityMapper(ClientFactory clientFactory){
		this.clientFactory = clientFactory;
	}

	@Override
	public void getActivity(final Place place, final ActivityCallbackHandler activityCallbackHandler) {
		HomepageActivity homepageActivity = clientFactory.getHomepageActivity();
		Runnable loadDataCallback = homepageActivity != null ? homepageActivity::loadData : null;
		
		
		if (place instanceof HomePlace) {
			activityCallbackHandler.onRecieveActivity( new HomeActivity(clientFactory, place));
		} 
		else if (place instanceof ContactPlace) {
			activityCallbackHandler.onRecieveActivity( new ContactActivity(clientFactory, place));
		} 
		else if (place instanceof HomepagePlace) {
			activityCallbackHandler.onRecieveActivity(new HomepageActivity(clientFactory, place));
		}
		else if (place instanceof AddUpdateContactPlace2) {
			
		    AddUpdateContactPlace2 myPlace = (AddUpdateContactPlace2) place;

		    activityCallbackHandler.onRecieveActivity(
		        new AddUpdateContactActivity2(
		            clientFactory,
		            place,
		            clientFactory.getGreetingService(), // Lấy service
		            clientFactory.getDataProvider(), // ListDataProvider<ContactInfo>
		            myPlace.getActionType(),
		            loadDataCallback
		            // actionType (ADD / UPDATE)
		            // Hoặc null nếu chưa cần callback
		        )
		    );
		}
		else if (place instanceof DeleteContactPlace2) {
			DeleteContactPlace2 myPlace = (DeleteContactPlace2) place;

		    activityCallbackHandler.onRecieveActivity(
		        new DeleteContactActivity2(
		                clientFactory,
		                (DeleteContactPlace2) place,
		                clientFactory.getGreetingService(),
		                clientFactory.getDataProvider(),
		                loadDataCallback
		            ));
		}
	}
}

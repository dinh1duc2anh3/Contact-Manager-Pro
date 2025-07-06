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


public class NormalAppActivityMapper implements AsyncActivityMapper {
	
	private ClientFactory clientFactory;
	
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

		    if (homepageActivity == null) {
		        homepageActivity = new HomepageActivity(clientFactory, place);
		        clientFactory.setHomePageActivity(homepageActivity);
		    } 
//		    homepageActivity.setPlace(place); // Nếu bạn cần cập nhật place mới
//	        homepageActivity.loadData();  // xóa đi để trogn homepage load thì sẽ ko bị lặp 2 lần nữa 

		    activityCallbackHandler.onRecieveActivity(homepageActivity);
		
		}
		else if (place instanceof AddUpdateContactPlace2) {
            AddUpdateContactPlace2 addUpdatePlace = (AddUpdateContactPlace2) place;
            AddUpdateContactActivity2 addUpdateActivity = clientFactory.getAddUpdateContactActivity2();
            if (addUpdateActivity == null) {
                addUpdateActivity = new AddUpdateContactActivity2(
                    clientFactory, place, addUpdatePlace.getActionType());
                clientFactory.setAddUpdateContactActivity2(addUpdateActivity);
                GWT.log("Created new AddUpdateContactActivity2 instance");
            } else {
                // Cập nhật dữ liệu cho instance hiện có
            	addUpdateActivity.setPlace(place);
                addUpdateActivity.setActionType(addUpdatePlace.getActionType());
                String phone = addUpdatePlace.getPhoneNumber();
                addUpdateActivity.setSelectedContact((phone != null) ? ContactInfoCache.getByPhoneNumber(phone) : null);
                addUpdateActivity.loadData();
                GWT.log("Reusing AddUpdateContactActivity2 instance");
            }
            activityCallbackHandler.onRecieveActivity(addUpdateActivity);
        }
		else if (place instanceof DeleteContactPlace2) {
            DeleteContactPlace2 deletePlace = (DeleteContactPlace2) place;
            DeleteContactActivity2 deleteActivity = clientFactory.getDeleteContactActivity2();
            if (deleteActivity == null) {
                deleteActivity = new DeleteContactActivity2(
                    clientFactory, deletePlace);
                clientFactory.setDeleteContactActivity2(deleteActivity);
                GWT.log("Created new DeleteContactActivity2 instance");
            } else {
                // Cập nhật dữ liệu cho instance hiện có
                deleteActivity.setSelectedPhoneNumbers(deletePlace.getPhoneNumbers()) ;
                deleteActivity.start(null, null); // Gọi start để cập nhật UI
                GWT.log("Reusing DeleteContactActivity2 instance");
            }
            activityCallbackHandler.onRecieveActivity(deleteActivity);
        }
	}
}

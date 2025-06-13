package com.hello.client.activities.contact;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.hello.client.activities.PlaceToken;
import com.hello.client.activities.basic.BasicPlace;
import com.hello.shared.model.User;

public class ContactPlace extends BasicPlace {
	
	private User user;
	
	public ContactPlace() {
	}
	
	public ContactPlace(User user) {
		super();
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public String getToken() {
		return PlaceToken.CONTACT;
	}
	
	public static class Tokenizer implements PlaceTokenizer<ContactPlace> {
        @Override
        public String getToken(ContactPlace place) {
            return place.getToken();
        }

        @Override
        public ContactPlace getPlace(String token) {
            return new ContactPlace();
        }
    }

}

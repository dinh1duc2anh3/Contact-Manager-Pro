package com.hello.client.activities.home;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.hello.client.activities.PlaceToken;
import com.hello.client.activities.basic.BasicPlace;

public class HomePlace extends BasicPlace {
	
	public HomePlace() {
		super();
	}
	
	@Override
	public String getToken() {
		return PlaceToken.HOME;
	}
	
	public static class Tokenizer implements PlaceTokenizer<HomePlace> {
        @Override
        public String getToken(HomePlace place) {
            return place.getToken();
        }

        @Override
        public HomePlace getPlace(String token) {
            return new HomePlace();
        }
    }
}

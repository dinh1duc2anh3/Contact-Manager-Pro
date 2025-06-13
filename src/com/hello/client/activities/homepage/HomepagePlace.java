package com.hello.client.activities.homepage;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class HomepagePlace extends Place {

    public HomepagePlace() {
        super();
    }

    public String getToken() {
        return "homepage"; // Define a unique token for the homepage
    }

    public static class Tokenizer implements PlaceTokenizer<HomepagePlace> {
        @Override
        public String getToken(HomepagePlace place) {
            return place.getToken();
        }

        @Override
        public HomepagePlace getPlace(String token) {
            return new HomepagePlace();
        }
    }
}
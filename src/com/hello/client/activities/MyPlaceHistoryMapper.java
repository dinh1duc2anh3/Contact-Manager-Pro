package com.hello.client.activities;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.hello.client.activities.contact.ContactPlace;
import com.hello.client.activities.home.HomePlace;
import com.hello.client.activities.homepage.HomepagePlace;
import com.hello.shared.model.ContactInfo;

@WithTokenizers({
    HomepagePlace.Tokenizer.class,
    HomePlace.Tokenizer.class,
    ContactPlace.Tokenizer.class,
    // Add others here when needed
})
public interface MyPlaceHistoryMapper extends PlaceHistoryMapper {
}

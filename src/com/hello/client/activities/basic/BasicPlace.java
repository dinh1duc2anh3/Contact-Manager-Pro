package com.hello.client.activities.basic;

import com.google.gwt.place.shared.Place;
import com.hello.client.activities.PlaceToken;

public class BasicPlace extends Place {
	
	public BasicPlace() {
		super();
	}
	
	public String getToken() {
		return PlaceToken.HOMEPAGE;
	}
}


package com.hello.client.activities.addupdate2;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.hello.shared.enums.ActionType;

public class AddUpdateContactPlace2 extends Place {
	private final ActionType actionType;
    private final String phoneNumber; 
    
    public AddUpdateContactPlace2(ActionType actionType, String phoneNumber) {
        this.actionType = actionType;
        this.phoneNumber = phoneNumber;
    }
    
    public ActionType getActionType() {
        return actionType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public static class Tokenizer implements PlaceTokenizer<AddUpdateContactPlace2> {
        @Override
        public String getToken(AddUpdateContactPlace2 place) {
            // VD: add-update:add hoặc add-update:update:0123456789
            if (place.getActionType() == ActionType.ADD) {
                return "add-update:add";
            } else {
                return "add-update:update:" + place.getPhoneNumber();
            }
        }

        @Override
        public AddUpdateContactPlace2 getPlace(String token) {
            // Tách token thành mảng
            String[] parts = token.split(":");
            if (parts.length >= 2) {
                ActionType type = ActionType.valueOf(parts[1].toUpperCase());
                String phone = (parts.length == 3) ? parts[2] : null;
                return new AddUpdateContactPlace2(type, phone);
            }
            return null;
        }
    }
	
}

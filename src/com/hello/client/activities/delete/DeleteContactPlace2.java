package com.hello.client.activities.delete;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class DeleteContactPlace2 extends Place {
    private final Set<String> phoneNumbers;

    public DeleteContactPlace2(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    @Prefix("delete")
    public static class Tokenizer implements PlaceTokenizer<DeleteContactPlace2> {
        @Override
        public DeleteContactPlace2 getPlace(String token) {
        	Set<String> phones = Arrays.stream(token.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            return new DeleteContactPlace2(phones);
        }

        @Override
        public String getToken(DeleteContactPlace2 place) {
            return String.join(",", place.getPhoneNumbers());
        }
    }
}
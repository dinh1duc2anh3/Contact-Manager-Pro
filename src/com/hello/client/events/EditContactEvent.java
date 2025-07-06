package com.hello.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.hello.shared.model.ContactInfo;

public class EditContactEvent extends GwtEvent<EditContactEvent.Handler> {
    public interface Handler extends EventHandler {
        void onEditContact(ContactInfo contact);
    }

    public static final Type<Handler> TYPE = new Type<>();

    private final ContactInfo contact;

    public EditContactEvent(ContactInfo contact) {
        this.contact = contact;
    }

    public ContactInfo getContact() {
        return contact;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onEditContact(contact);
    }
}
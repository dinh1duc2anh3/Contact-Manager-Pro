package com.hello.client.events;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.hello.shared.model.ContactInfo;

public class ClickFirstNameColumnEvent extends GwtEvent<ClickFirstNameColumnEvent.Handler> {
    public interface Handler extends EventHandler {
        void onClickFirstNameColumn(ContactInfo contact);
    }

    public static final Type<Handler> TYPE = new Type<>();

    private final ContactInfo contact;

    public ClickFirstNameColumnEvent(ContactInfo contact) {
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
        handler.onClickFirstNameColumn(contact);
    }
}
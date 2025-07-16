package com.hello.client.activities.basic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.hello.client.AppManager;
import com.hello.client.activities.ClientFactory;
import com.hello.client.events.ActionEvent;
import com.hello.client.events.ActionEventHandler;

public class BasicActivity extends MGWTAbstractActivity {
	
	protected ClientFactory clientFactory;
	protected EventBus eventBus;
	protected Place place;
	protected List<HandlerRegistration> handlerRegistrations = new ArrayList<>();
	
	public BasicActivity(ClientFactory clientFactory, Place place) {
		super();
		this.clientFactory = clientFactory;
		this.place = place;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void start(AcceptsOneWidget panel, EventBus eventBus, final BasicView basicView) {
		this.eventBus = eventBus;
	}

	protected void bind() {
		addHandlerRegistration(eventBus.addHandler(ActionEvent.TYPE, new ActionEventHandler() {
			@Override
			public void onEvent(ActionEvent event) {
				Window.alert("Event : "  + event.getUser().getName());
			}
		}));
	}
	
	protected void addHandlerRegistration(HandlerRegistration registration) {
        handlerRegistrations.add(registration);
    }

    protected void clearHandlers() {
        for (HandlerRegistration registration : handlerRegistrations) {
            registration.removeHandler();
        }
        handlerRegistrations.clear();
    }

    @Override
    public void onStop() {
        clearHandlers();
    }
	
	public void loadData() {
		
	}
	
	protected void refreshView() {
		
	}
	
	protected void gotoPage(Place newPlace) {
		AppManager.CLIENT_FACTORY.getPlaceController().goTo(newPlace);
	}
}
package com.hello.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.hello.client.activities.AppPlaceHistoryMapper;
import com.hello.client.activities.AsyncActivityManager;
import com.hello.client.activities.AsyncActivityMapper;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.ClientFactoryImpl;
import com.hello.client.activities.MyPlaceHistoryMapper;
import com.hello.client.activities.NormalAppActivityMapper;
import com.hello.client.activities.basic.widget.LayoutWrapper;
import com.hello.client.activities.homepage.HomepagePlace;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AppManager implements EntryPoint {
	
	public static final GreetingServiceAsync GREETING_SERVICES = GWT.create(GreetingService.class);
	public static final ClientFactory CLIENT_FACTORY = new ClientFactoryImpl();
	
	public void onModuleLoad() {
		// set up layout + singleton 
		LayoutWrapper layout = LayoutWrapper.getOnlyLayoutWrapper();
		SimplePanel mainPanel = new SimplePanel();
		
		layout.setMainContent(mainPanel);
		
		// setup activity management
		AsyncActivityMapper activityMapper = new NormalAppActivityMapper(CLIENT_FACTORY);
		AsyncActivityManager activityManager = new AsyncActivityManager(activityMapper, CLIENT_FACTORY.getEventBus());
		activityManager.setDisplay(mainPanel);
		
		// setup place history
		final PlaceHistoryMapper myHistoryMapper = GWT.create(MyPlaceHistoryMapper.class);
		PlaceHistoryMapper historyMapper = new AppPlaceHistoryMapper(myHistoryMapper);
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(CLIENT_FACTORY.getPlaceController(), CLIENT_FACTORY.getEventBus(), CLIENT_FACTORY.getHomepagePlace());
		historyHandler.handleCurrentHistory();
	}

}

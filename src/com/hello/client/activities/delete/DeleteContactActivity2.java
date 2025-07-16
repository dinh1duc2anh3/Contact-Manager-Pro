package com.hello.client.activities.delete;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.ClientFactoryImpl;
import com.hello.client.activities.basic.BasicActivity;
import com.hello.client.activities.homepage.HomepagePlace;
import com.hello.shared.utils.OverlayUtil;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;

public class DeleteContactActivity2 extends BasicActivity  {
    private GreetingServiceAsync greetingService = clientFactory.getGreetingService();
    private ListDataProvider<ContactInfo> dataProvider = clientFactory.getDataProvider();
    private DeleteContactView2 view ;
    private Set<String> selectedPhoneNumbers;

	public DeleteContactActivity2(
        ClientFactory clientFactory,
        Place place) {
        super(clientFactory, place);
        this.view =  clientFactory.getDeleteContactView2();
        this.selectedPhoneNumbers = ( (DeleteContactPlace2) place).getPhoneNumbers(); // Lấy từ URL
        
        if (view == null ) {
            GWT.log("View is null");
            throw new IllegalStateException("DeleteContactView2 not fully initialized");
        }
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view.asWidget());
        GWT.log("DeleteContactActivity2 - bind3");
        if (handlerRegistrations.isEmpty()) {
            bind();
            GWT.log("DeleteContactActivity2 - bind3 - done");
        }
        view.getConfirmLabel().setText("Xác nhận xoá " + selectedPhoneNumbers.size() + " liên hệ?");
    }
    
    @Override
    protected void bind() {
    	GWT.log("1");
        if (view.getYesButton() == null || view.getNoButton() == null) {
            GWT.log("Error: YesButton or NoButton is null");
            return;
        }
        GWT.log("2");
        addHandlerRegistration(view.getYesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDelete();
            }
        }));
        GWT.log("3");
        addHandlerRegistration(view.getNoButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clientFactory.getPlaceController().goTo(new HomepagePlace());
            }
        }));
    }
    
    private void onDelete() {
    	List<String> listPhones = new ArrayList<>();
        for (String phone: selectedPhoneNumbers ) {
        	listPhones.add(phone);
        }
    	
        greetingService.deleteContacts(listPhones, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Xoá thất bại: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                ContactInfoCache.removeContactByPhoneNumbers(listPhones); // bạn tự implement nếu cần
                dataProvider.getList().removeIf(c -> selectedPhoneNumbers.contains(c.getPhoneNumber()));
                dataProvider.refresh();
                Window.alert("Đã xoá thành công");
                clientFactory.getPlaceController().goTo(new HomepagePlace());
            }
        });
    }
    
    public Set<String> getSelectedPhoneNumbers() {
		return selectedPhoneNumbers;
	}

}

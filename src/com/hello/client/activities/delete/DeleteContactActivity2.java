package com.hello.client.activities.delete;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
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
import com.hello.client.activities.deleteDialog.DeleteContactView;
import com.hello.client.activities.homepage.HomepagePlace;
import com.hello.shared.utils.OverlayUtil;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;

public class DeleteContactActivity2 extends BasicActivity  {
    private DeleteContactView2 view;
    private GreetingServiceAsync greetingService;
    private Set<String> selectedPhoneNumbers;
    private ListDataProvider<ContactInfo> dataProvider;

	public DeleteContactActivity2(
        ClientFactory clientFactory,
        DeleteContactPlace2 place
    ) {
        super(clientFactory, place);
        this.view = clientFactory.getDeleteContactView2();
        if (this.view == null) {
            GWT.log("Error: DeleteContactView2 is null");
            throw new IllegalStateException("DeleteContactView2 is null");
        }
        this.greetingService = clientFactory.getGreetingService();
        this.dataProvider = clientFactory.getDataProvider();
        this.selectedPhoneNumbers = place.getPhoneNumbers(); // Lấy từ URL
        
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
        if (view.getYesButton() == null || view.getNoButton() == null) {
            GWT.log("Error: YesButton or NoButton is null");
            return;
        }
        super.bind();
        addHandlerRegistration(view.getYesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onDelete();
            }
        }));
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

	public void setSelectedPhoneNumbers(Set<String> selectedPhoneNumbers) {
		this.selectedPhoneNumbers = selectedPhoneNumbers;
	}

}

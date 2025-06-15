package com.hello.client.activities.crud;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.hello.shared.utils.OverlayUtil;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;

public class DeleteContactActivity {
	
	
    private GreetingServiceAsync greetingService;
    
    private ListDataProvider<ContactInfo> dataProvider ;
    private MultiSelectionModel<ContactInfo> multiSelectionModel ;
    private Set<ContactInfo> selectedContacts ;
    private List<String> selectedPhoneNumbers = new ArrayList<>();
    
    ClientFactory clientFactory = new ClientFactoryImpl();
    DeleteContactView view = clientFactory.getDeleteContactView();
    
    public DeleteContactActivity(
    		GreetingServiceAsync greetingService, 
//    		Button deleteContactButton,
    		ListDataProvider<ContactInfo> dataProvider, 
    		MultiSelectionModel<ContactInfo> multiSelectionModel,
    		SimplePanel overlay
    		) {
    	
        this.greetingService = greetingService;
        this.dataProvider = dataProvider;
        this.multiSelectionModel = multiSelectionModel;
        this.selectedContacts = multiSelectionModel.getSelectedSet();
        view.setOverlay(overlay);
        // extract phone numbers only to send ( id )
    	for (ContactInfo contactInfo : selectedContacts) {
    		this.selectedPhoneNumbers.add(contactInfo.getPhoneNumber());
    	}
    	
    	view.initDialogStyle();
    	
    	bind();
        
    }

    
    private void bind() {
    	view.getDeleteButton().addClickHandler(event -> onDeleteButtonClick());
    	view.getCloseButton().addClickHandler(event -> closeDialog());
	}

    void onDeleteButtonClick() {
		// delete selected contact by phone number
		greetingService.deleteContacts(selectedPhoneNumbers,new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				GWT.log("Error: connect server - deleting contact info ");
				Window.alert("Error: deleting contact");
			}

			public void onSuccess(Void result) {
				Window.alert("Success: deleting contact");
				GWT.log("Success: deleting contact");
				dataProvider.getList().removeAll(selectedContacts); 
				dataProvider.refresh();
				ContactInfoCache.removeContacts(selectedContacts);
				closeDialog();
			}
		});
    }

	void closeDialog() {
		multiSelectionModel.clear();
		view.closeDialogBox();
    }

    public void showDialog() {
        view.showDialogBox();
    }
    
	
	
	
}

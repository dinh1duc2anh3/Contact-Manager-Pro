package koolsoft.client.view;
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

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.client.util.OverlayUtil;
import koolsoft.shared.ContactInfo;

public class DeleteContactDialog {
	
	interface DeleteContactInfoDialogUiBinder extends UiBinder<Widget, DeleteContactDialog> {}
    private static DeleteContactInfoDialogUiBinder uiBinder = GWT.create(DeleteContactInfoDialogUiBinder.class);
    
    private GreetingServiceAsync greetingService;
    private ListDataProvider<ContactInfo> dataProvider ;
    private MultiSelectionModel<ContactInfo> multiSelectionModel ;
    private Set<ContactInfo> selectedContacts ;
    private List<String> selectedPhoneNumbers = new ArrayList<>();
    
    private Button deleteContactButton;
    private SimplePanel overlay;
    
    @UiField
    DialogBox dialogBox;
    @UiField
    Label deleteConfirmLabel;
    @UiField
    Button closeDeleteInfoButton;
    @UiField
    Button deleteInfoButton;
    
    public DeleteContactDialog(GreetingServiceAsync greetingService, Button deleteContactButton,
    		ListDataProvider<ContactInfo> dataProvider, 
    		MultiSelectionModel<ContactInfo> multiSelectionModel, SimplePanel overlay) {
        // Bind the UI elements defined in the UI file
        uiBinder.createAndBindUi(this);
        this.greetingService = greetingService;
        this.deleteContactButton = deleteContactButton;
        this.dataProvider = dataProvider;
        this.multiSelectionModel = multiSelectionModel;
        this.selectedContacts = multiSelectionModel.getSelectedSet();
        this.overlay = overlay;
        
        // extract phone numbers only to send ( id )
    	for (ContactInfo contactInfo : selectedContacts) {
    		this.selectedPhoneNumbers.add(contactInfo.getPhoneNumber());
    	}
        
    }

    @UiHandler("closeDeleteInfoButton")
    void onCloseButtonClick(ClickEvent event) {
    	closeDialog();
        
    }
    
    void closeDialog() {
        deleteContactButton.setEnabled(true);
		multiSelectionModel.clear();
		OverlayUtil.removeOverlay(overlay);
    	dialogBox.hide();
    }

    // UI Handler for Delete button
    @UiHandler("deleteInfoButton")
    void onDeleteButtonClick(ClickEvent event) {
   
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

				closeDialog();
			}
		});
    }

    public void showDialog() {
    	OverlayUtil.displayOverlay(overlay);
        dialogBox.center();
        dialogBox.show();
        deleteContactButton.setEnabled(false);
    }
    
	
	
	
}

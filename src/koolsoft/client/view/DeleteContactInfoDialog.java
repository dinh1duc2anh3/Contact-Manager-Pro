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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.shared.ContactInfoDTO;

public class DeleteContactInfoDialog {
	
	interface DeleteContactInfoDialogUiBinder extends UiBinder<Widget, DeleteContactInfoDialog> {}
    private static DeleteContactInfoDialogUiBinder uiBinder = GWT.create(DeleteContactInfoDialogUiBinder.class);
    
    private GreetingServiceAsync greetingService = null;
    private ListDataProvider<ContactInfoDTO> dataProvider = null;
    private Button deleteContactButton;
    
    private MultiSelectionModel<ContactInfoDTO> multiSelectionModel = null;
    
    private Set<ContactInfoDTO> selectedContacts = null;
    private List<String> selectedPhoneNumbers = new ArrayList<>();
    
    @UiField
    DialogBox dialogBox;
    @UiField
    Label deleteConfirmLabel;
    @UiField
    Button closeDeleteInfoButton;
    @UiField
    Button deleteInfoButton;
    
    public DeleteContactInfoDialog(GreetingServiceAsync greetingService, Button deleteContactButton,ListDataProvider<ContactInfoDTO> dataProvider,Set<ContactInfoDTO> selectedContacts, MultiSelectionModel<ContactInfoDTO> multiSelectionModel) {
        // Bind the UI elements defined in the UI file
        uiBinder.createAndBindUi(this);
        this.greetingService = greetingService;
        this.deleteContactButton = deleteContactButton;
        this.dataProvider = dataProvider;
        this.selectedContacts = selectedContacts;
        this.multiSelectionModel = multiSelectionModel;
        
        // extract phone numbers only to send ( id )
    	for (ContactInfoDTO contactInfo : selectedContacts) {
    		this.selectedPhoneNumbers.add(contactInfo.getPhoneNumber());
    	}
        
    }

    @UiHandler("closeDeleteInfoButton")
    void onCloseButtonClick(ClickEvent event) {
        dialogBox.hide();
        deleteContactButton.setEnabled(true);
		
		multiSelectionModel.clear();
    }

    // UI Handler for Delete button
    @UiHandler("deleteInfoButton")
    void onDeleteButtonClick(ClickEvent event) {
   
		// delete selected contact by phone number
		greetingService.deleteContacts(selectedPhoneNumbers,new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				GWT.log("Error: connect server - deleting contact info ");
			}

			public void onSuccess(Void result) {
				Window.alert("Success: deleting contact");
				GWT.log("Success: deleting contact");
				dataProvider.getList().removeAll(selectedContacts); 
				dataProvider.refresh();
				
				multiSelectionModel.clear();
				
				//hide add contact info dialogbox
				dialogBox.hide();
		        deleteContactButton.setEnabled(true);
			}
		});
    }

    public void showDialog() {
        dialogBox.center();
        dialogBox.show();
        deleteContactButton.setEnabled(false);
    }
    
	
	
	
}

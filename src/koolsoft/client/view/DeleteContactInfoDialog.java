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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.shared.ContactInfo;

public class DeleteContactInfoDialog {
	
	interface DeleteContactInfoDialogUiBinder extends UiBinder<Widget, DeleteContactInfoDialog> {}
    private static DeleteContactInfoDialogUiBinder uiBinder = GWT.create(DeleteContactInfoDialogUiBinder.class);
    
    private GreetingServiceAsync greetingService = null;
    private ListDataProvider<ContactInfo> dataProvider = null;
    private Button deleteContactButton;
    private SimplePanel overlay;
    
    private MultiSelectionModel<ContactInfo> multiSelectionModel = null;
    
    private Set<ContactInfo> selectedContacts = null;
    private List<String> selectedPhoneNumbers = new ArrayList<>();
    
    @UiField
    DialogBox dialogBox;
    @UiField
    Label deleteConfirmLabel;
    @UiField
    Button closeDeleteInfoButton;
    @UiField
    Button deleteInfoButton;
    
    public DeleteContactInfoDialog(GreetingServiceAsync greetingService, Button deleteContactButton,
    		ListDataProvider<ContactInfo> dataProvider,Set<ContactInfo> selectedContacts, 
    		MultiSelectionModel<ContactInfo> multiSelectionModel, SimplePanel overlay) {
        // Bind the UI elements defined in the UI file
        uiBinder.createAndBindUi(this);
        this.greetingService = greetingService;
        this.deleteContactButton = deleteContactButton;
        this.dataProvider = dataProvider;
        this.selectedContacts = selectedContacts;
        this.multiSelectionModel = multiSelectionModel;
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
		// Đảm bảo xóa overlay khỏi RootPanel
        if (overlay != null && RootPanel.get().getWidgetIndex(overlay) != -1) {
            RootPanel.get().remove(overlay);
        }
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
    	// Đảm bảo hiển thị overlay
        if (overlay != null && RootPanel.get().getWidgetIndex(overlay) == -1) {
            RootPanel.get().add(overlay);
        }
        dialogBox.center();
        dialogBox.show();
        deleteContactButton.setEnabled(false);
    }
    
	
	
	
}

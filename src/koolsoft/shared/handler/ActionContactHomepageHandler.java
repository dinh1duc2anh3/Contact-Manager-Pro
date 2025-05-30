package koolsoft.shared.handler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.client.util.OverlayUtil;
import koolsoft.client.view.AddUpdateContactDialog;
import koolsoft.client.view.DeleteContactDialog;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.enums.ActionType;

public class ActionContactHomepageHandler implements ClickHandler {
	private GreetingServiceAsync greetingService;
	private ListDataProvider<ContactInfo> dataProvider;
	private MultiSelectionModel<ContactInfo> multiSelectionModel;
	private Button actionContactButton;
	private ActionType actionType;

	public ActionContactHomepageHandler(ListDataProvider<ContactInfo> dataProvider,
			MultiSelectionModel<ContactInfo> multiSelectionModel, Button actionContactButton,
			GreetingServiceAsync greetingService, ActionType actionType) {
		this.greetingService = greetingService;
		this.dataProvider = dataProvider;
		this.multiSelectionModel = multiSelectionModel;
		this.actionContactButton = actionContactButton;
		this.actionType = actionType;
	}

	@Override
	public void onClick(ClickEvent event) {
		try {

			// Validate selection state before proceeding
			if (!isValidSelectionForAction()) {
				return;
			}

			// Tạo overlay element
			SimplePanel overlay = OverlayUtil.createOverlay();
			

			if (actionType.equals(ActionType.ADD)) {
				// For ADD, we don't need selected contacts
				AddUpdateContactDialog addContactInfoDialog = new AddUpdateContactDialog(greetingService,
						actionContactButton, dataProvider, multiSelectionModel, ActionType.ADD, overlay);
				addContactInfoDialog.showDialog();
			} 
			else if (actionType.equals(ActionType.UPDATE)) {
				// For UPDATE, we need exactly one selected contact
				if (multiSelectionModel.getSelectedSet().isEmpty()) {
					Window.alert("Please select a contact to update.");
//					OverlayUtil.removeOverlay(overlay);
					return;
				}
				AddUpdateContactDialog updateContactInfoDialog = new AddUpdateContactDialog(greetingService,
						actionContactButton, dataProvider, multiSelectionModel, ActionType.UPDATE, overlay);
				updateContactInfoDialog.showDialog();
			} 
			else if (actionType.equals(ActionType.DELETE)) {
				// Create the dialog
				DeleteContactDialog deleteContactInfoDialog = new DeleteContactDialog(greetingService,
						actionContactButton, dataProvider, multiSelectionModel, overlay);

				deleteContactInfoDialog.showDialog();
			}
		} catch (Exception e) {
			GWT.log("Error creating action dialog: " + e.getMessage(), e);
			Window.alert("Error opening action dialog: " + e.getMessage());
		}

	}
	
	private boolean isValidSelectionForAction() {
		int selectedCount = multiSelectionModel.getSelectedSet().size();
		
		switch (actionType) {
			case ADD:
				// ADD should work regardless of selection
				return true;
			case UPDATE:
				// UPDATE requires exactly one selection
				if (selectedCount != 1) {
					Window.alert("Please select exactly one contact to update.");
					return false;
				}
				return true;
			case DELETE:
				// DELETE requires at least one selection
				if (selectedCount == 0) {
					Window.alert("Please select at least one contact to delete.");
					return false;
				}
				return true;
			default:
				return false;
		}
	}
	
	
}

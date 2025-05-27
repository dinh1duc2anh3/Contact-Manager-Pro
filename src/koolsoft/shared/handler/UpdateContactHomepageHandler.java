package koolsoft.shared.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.client.view.AddUpdateContactDialog;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.enums.ActionType;

public class UpdateContactHomepageHandler implements ClickHandler {
	private ListDataProvider<ContactInfo> dataProvider;
	private MultiSelectionModel<ContactInfo> multiSelectionModel;
	
	private Button updateContactButton;
	
	private GreetingServiceAsync greetingService;
	
	private ContactInfo selectedContact;


	public UpdateContactHomepageHandler(ListDataProvider<ContactInfo> dataProvider,
			MultiSelectionModel<ContactInfo> multiSelectionModel, Button updateContactButton,
			GreetingServiceAsync greetingService) {
		this.dataProvider = dataProvider;
		this.multiSelectionModel = multiSelectionModel;
		this.updateContactButton = updateContactButton;
		this.greetingService = greetingService;
		this.selectedContact = multiSelectionModel.getSelectedSet().iterator().next();
	}


	@Override
	public void onClick(ClickEvent event) {
		// Tạo overlay element
		SimplePanel overlay = new SimplePanel();
		overlay.getElement().setId("dialogOverlay");
		overlay.setStyleName("modal-overlay");
//
//		// Thêm overlay vào RootPanel trước khi hiển thị dialog
//		if (RootPanel.get().getWidgetIndex(overlay) == -1) {
//			RootPanel.get().add(overlay);
//		}

		AddUpdateContactDialog updateContactInfoDialog = new AddUpdateContactDialog(greetingService,
				updateContactButton, dataProvider,  multiSelectionModel, ActionType.UPDATE,
				overlay);
		updateContactInfoDialog.showDialog();
	}



}

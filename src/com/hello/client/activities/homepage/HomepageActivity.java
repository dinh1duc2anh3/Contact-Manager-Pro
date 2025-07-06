package com.hello.client.activities.homepage;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.MultiSelectionModel;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.hello.client.ClientUtils;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.addupdate2.AddUpdateContactPlace2;
import com.hello.client.activities.addupdateDialog.AddUpdateContactActivity;
import com.hello.client.activities.basic.BasicActivity;
import com.hello.client.activities.contact.ContactPlace;
import com.hello.client.activities.delete.DeleteContactPlace2;
import com.hello.client.activities.home.HomeView;
import com.hello.client.events.EditContactEvent;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.model.User;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.enums.ActionType;
import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.handler.ActionContactHomepageHandler;
import com.hello.shared.handler.LiveSearchContactHandler;
import com.hello.shared.handler.SearchContactHandler;
import com.hello.shared.handler.ServerSearchContactHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HomepageActivity extends BasicActivity {

	private HomepageView view;
    private final GreetingServiceAsync greetingService;
    private ListDataProvider<ContactInfo> dataProvider;
    private Set<ContactInfo> selectedContacts;
  
    public HomepageActivity(ClientFactory clientFactory, Place place) {
		super(clientFactory, place);
		this.place = new HomepagePlace();
		this.greetingService = clientFactory.getGreetingService();
		this.dataProvider = clientFactory.getDataProvider();
		clientFactory.setHomePageActivity(this); // important
	}

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.view = clientFactory.getHomepageView(); // Assume ClientFactory provides HomepageView
        panel.setWidget(view.asWidget());
        view.setupUI();
        view.setupContactTable(dataProvider); 

		GWT.log("HomepageActivity - bind1");
		bind();
        loadData();
    }

    @Override
    public void onStop() {
        GWT.log("HomepageActivity - onStop: Removing handlers");
        clientFactory.setHomePageActivity(null);
        super.onStop();
    }

    @Override
	protected void bind() {
    	if (view.getSearchBox() == null || view.getSearchButton() == null || view.getClearSearchButton() == null ||
    	        view.getAddContactButton() == null || view.getUpdateContactButton() == null ||
    	        view.getDeleteContactButton() == null || view.getToggleSelectAllButton() == null ||
    	        view.getGenderValueListBox() == null || view.getAddressValueListBox() == null ||
    	        view.getResetFilterButton() == null) {
    	        GWT.log("Error: One or more UI components in HomepageView is null");
    	        return;
    	    }
    	
    	// Search Handlers
        addHandlerRegistration(view.getSearchBox().getTextBox().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                view.getSpinner().getStyle().setDisplay(Display.BLOCK);
                Scheduler.get().scheduleFixedDelay(() -> {
                    view.getSpinner().getStyle().setDisplay(Display.NONE);
                    return false;
                }, 300);
            }
        }));

        addHandlerRegistration(view.getSearchBox().getTextBox().addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    GWT.log("Enter pressed – triggering search button click");
                    view.getSearchButton().click();
                }
            }
        }));
        
        
        addHandlerRegistration(view.getClearSearchButton().addClickHandler(event -> loadData()));

        addHandlerRegistration(view.getSearchButton().addClickHandler(new SearchContactHandler(
                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel(), greetingService)));

        // Selection Change Handler
        addHandlerRegistration(view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                selectedContacts = view.getSelectionModel().getSelectedSet();
                view.getUpdateContactButton().setEnabled(selectedContacts.size() == 1);
                view.getDeleteContactButton().setEnabled(!selectedContacts.isEmpty());
                view.getAddContactButton().setEnabled(selectedContacts.isEmpty());
            }
        }));
        
        
     // Action Handlers
        addHandlerRegistration(view.getAddContactButton().addClickHandler(event ->
                clientFactory.getPlaceController().goTo(new AddUpdateContactPlace2(ActionType.ADD, null))));

        addHandlerRegistration(view.getUpdateContactButton().addClickHandler(event -> {
            Set<ContactInfo> selected = view.getSelectionModel().getSelectedSet();
            ContactInfo contact = selected.iterator().next();
            clientFactory.getPlaceController().goTo(new AddUpdateContactPlace2(ActionType.UPDATE, contact.getPhoneNumber()));
        }));

        addHandlerRegistration(view.getDeleteContactButton().addClickHandler(event -> {
        	if (view.getDeleteContactButton() == null) {
                GWT.log("Error: DeleteContactButton is null");
                return;
            }
            GWT.log("DeleteContactButton clicked");
            Set<ContactInfo> selected = view.getSelectionModel().getSelectedSet();
            Set<String> phoneNumbers = selected.stream().map(ContactInfo::getPhoneNumber).collect(Collectors.toSet());
            clientFactory.getPlaceController().goTo(new DeleteContactPlace2(phoneNumbers));
        }));

        
     // Select All Handler
        addHandlerRegistration(view.getToggleSelectAllButton().addClickHandler(event -> toggleSelectAllContacts()));

        // Filter Handlers
        addHandlerRegistration(view.getGenderValueListBox().addValueChangeHandler(e -> applyFilters()));
        addHandlerRegistration(view.getAddressValueListBox().addValueChangeHandler(e -> applyFilters()));
        addHandlerRegistration(view.getResetFilterButton().addClickHandler(e -> resetFilters()));

        // EditContactEvent Handler
        addHandlerRegistration(clientFactory.getEventBus().addHandler(EditContactEvent.TYPE, new EditContactEvent.Handler() {
            @Override
            public void onEditContact(ContactInfo contact) {
                GWT.log("HomepageActivity: EditContactEvent fired for " + contact.getPhoneNumber());
                clientFactory.getPlaceController().goTo(new AddUpdateContactPlace2(ActionType.UPDATE, contact.getPhoneNumber()));
            }
        }));

        // Ctrl+F Handler (không dùng addHandlerRegistration vì là native JS)
        bindCtrlFToSearchBox(view.getSearchBox().getElement());

        // Setup filter controls
        setupFilterControls();
        
        

    }
    

	private void toggleSelectAllContacts() {
		@SuppressWarnings("unchecked")
		MultiSelectionModel<ContactInfo> selectionModel = (MultiSelectionModel<ContactInfo>) view.getContactTable().getSelectionModel();
		
		List<ContactInfo> allContacts = view.getContactTable().getVisibleItems();

		boolean hasSelection = false;
	    for (ContactInfo contact : allContacts) {
	        if (selectionModel.isSelected(contact)) {
	            hasSelection = true;
	            break;
	        }
	    }
	    if (hasSelection) {
	        // Deselect all
	        for (ContactInfo contact : allContacts) {
	            selectionModel.setSelected(contact, false);
	        }
	    } else {
	        // Select all
	        for (ContactInfo contact : allContacts) {
	            selectionModel.setSelected(contact, true);
	        }
	    }
	}

	private native void bindCtrlFToSearchBox(Element inputElement) /*-{
		$wnd.addEventListener("keydown", function(e) {
			if (e.ctrlKey && e.key === 'f') {
				e.preventDefault();
				inputElement.focus();
				inputElement.select();
			}
		});
	}-*/;

    private void setupFilterControls() {
    	view.getGenderValueListBox().setValue(null);
    	view.getGenderValueListBox().setAcceptableValues(Arrays.asList(Gender.values()));
    	
    	view.getAddressValueListBox().setAcceptableValues(Arrays.asList(Address.values()));
    	view.getAddressValueListBox().setValue(null);
	}

    private void applyFilters() {
        Gender selectedGender = view.getGenderValueListBox().getValue();
        Address selectedAddress = view.getAddressValueListBox().getValue();

        List<ContactInfo> filtered = ContactInfoCache.getCurrentContacts().stream()
            .filter(c -> {
                boolean matchGender = selectedGender == null || c.getGender().equals(selectedGender);
                boolean matchAddress = selectedAddress == null || c.getAddress().equals(selectedAddress);
                return matchGender && matchAddress;
            })
            .collect(Collectors.toList());

        dataProvider.getList().clear();
        dataProvider.getList().addAll(filtered);
        dataProvider.refresh();
    }
    
    private void resetFilters() {
        view.getGenderValueListBox().setValue(null);
        view.getAddressValueListBox().setValue(null);
        dataProvider.getList().clear();
        dataProvider.getList().addAll(ContactInfoCache.getCurrentContacts());
//        applyFilters();
    }
    
    
	@Override
    public void loadData() {
        greetingService.getAllContactInfos(new AsyncCallback<List<ContactInfo>>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error: initially getting all contact info");
                view.getErrorLabel().setText("Error fetching contacts: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<ContactInfo> result) {
                GWT.log("Success: initially getting all contact info from server");
                view.getSearchBox().setText("");
//                result.sort(Comparator.comparing(ContactInfo::getPhoneNumber));
                result.sort(Comparator.comparing(ContactInfo::getCreatedDate).reversed());
                dataProvider.getList().clear();
                
                dataProvider.getList().addAll(result);
                dataProvider.refresh();
                
                ContactInfoCache.setCurrentContacts(result);
                
                MultiWordSuggestOracle oracle =  (MultiWordSuggestOracle ) view.getSearchBox().getSuggestOracle() ;
                for (ContactInfo contact : ContactInfoCache.getCurrentContacts()) {
                	String fullName = contact.getFullName();
                	if (fullName != null && !fullName.isEmpty()) {
                	    oracle.add(fullName);
                	}

                	String phone = contact.getPhoneNumber();
                	if (phone != null && !phone.isEmpty()) {
                	    oracle.add(phone);
                	}
                }
            }
        });
    }
}
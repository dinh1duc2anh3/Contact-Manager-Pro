package com.hello.client.activities.homepage;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
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
import com.hello.client.activities.basic.BasicActivity;
import com.hello.client.activities.contact.ContactPlace;
import com.hello.client.activities.crud.AddUpdateContactActivity;
import com.hello.client.activities.home.HomeView;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.model.User;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.enums.ActionType;
import com.hello.shared.handler.ActionContactHomepageHandler;
import com.hello.shared.handler.LiveSearchContactHandler;
import com.hello.shared.handler.ServerSearchContactHandler;

import java.util.List;
import java.util.Set;

public class HomepageActivity extends BasicActivity {

	private HomepageView view;
    private final GreetingServiceAsync greetingService;
    private ListDataProvider<ContactInfo> dataProvider;
    private Set<ContactInfo> selectedContacts;
	
    public HomepageActivity(ClientFactory clientFactory, Place place) {
		super(clientFactory, place);
		this.place = new HomepagePlace();
		this.greetingService = clientFactory.getGreetingService();
		this.dataProvider = new ListDataProvider<>();
	}

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.view = clientFactory.getHomepageView(); // Assume ClientFactory provides HomepageView
        panel.setWidget(view.asWidget());
        setupUI();
        bind();
        loadData();
    }

    private void setupUI() {
        view.getSearchBox().setText("");
        // add placeholder
        view.getSearchBox().getElement().setPropertyString("placeholder", "Search by name or phone number...");
        view.getAddContactButton().setEnabled(true);
        view.getUpdateContactButton().setEnabled(false);
        view.getDeleteContactButton().setEnabled(false);
        setupContactTable();
    }

    private void setupContactTable() {
        CellTable<ContactInfo> contactTable = view.getContactTable();
        MultiSelectionModel<ContactInfo> selectionModel = view.getSelectionModel();
        contactTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<ContactInfo>createCheckboxManager());
        contactTable.setKeyboardSelectionPolicy(CellTable.KeyboardSelectionPolicy.ENABLED);

        dataProvider.addDataDisplay(contactTable);

        // Column: Checkbox
        Column<ContactInfo, Boolean> checkColumn = new Column<ContactInfo, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(ContactInfo object) {
                return selectionModel.isSelected(object);
            }
        };
        contactTable.addColumn(checkColumn, "CheckBox");

        // Column: First Name
        Column<ContactInfo, String> firstNameColumn = new Column<ContactInfo, String>(new ClickableTextCell()) {
            @Override
            public String getValue(ContactInfo contact) {
                return contact.getFirstName();
            }
        };

        
        firstNameColumn.setFieldUpdater((index, contact, value) -> {
        	if (selectionModel.getSelectedSet().size() == 1) {
        		SimplePanel overlay = new SimplePanel();
                AddUpdateContactActivity addContactActivity = new AddUpdateContactActivity(
    					greetingService,
    					dataProvider, 
    					selectionModel, 
    					ActionType.UPDATE, 
    					overlay);
    			addContactActivity.showDialog();
        	}
        });
        contactTable.addColumn(firstNameColumn, "First Name");

        // Column: Last Name
        TextColumn<ContactInfo> lastNameColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getLastName();
            }
        };
        contactTable.addColumn(lastNameColumn, "Last Name");
        
        // Column: gender
        TextColumn<ContactInfo> genderColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getGenderStr();
            }
        };
        contactTable.addColumn(genderColumn, "Gender");

        // Column: Phone Number
        TextColumn<ContactInfo> phoneNumberColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getPhoneNumber();
            }
        };
        contactTable.addColumn(phoneNumberColumn, "Phone Number");

        // Column: Address
        TextColumn<ContactInfo> addressColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getAddressStr();
            }
        };
        contactTable.addColumn(addressColumn, "Address");
    }

    @Override
	protected void bind() {
        // Server + Live Search Handler
    	initSearchHandler();

        // Selection Change Handler
        initSelectionModelHandler();

        // Action Handlers
        initAddUpdateDeleteHandlers();
        
        initSelectAllHandler();
        
        bindCtrlFToSearchBox(view.getSearchBox().getElement());
    }
    
    private void initSearchHandler() {	
    	
    	view.getSearchBox().getTextBox().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				view.getSpinner().getStyle().setDisplay(Display.BLOCK);
				
				// Hide spinner after a short delay if needed
		        Scheduler.get().scheduleFixedDelay(() -> {
		            view.getSpinner().getStyle().setDisplay(Display.NONE);
		            return false;
		        }, 300);
			}
		});
		// Server Search Handler
    	ServerSearchContactHandler serverSearchHandler = new ServerSearchContactHandler(
                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel(), greetingService);
        view.getServerSearchButton().addClickHandler(serverSearchHandler);

        // Live Search Handler
        LiveSearchContactHandler liveSearchHandler = new LiveSearchContactHandler(
                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel());
        view.getLiveSearchButton().addClickHandler(liveSearchHandler);
	}
    
    private void initSelectionModelHandler() {
    	view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                selectedContacts = view.getSelectionModel().getSelectedSet();
                if (selectedContacts.size() == 0) {
                    view.getUpdateContactButton().setEnabled(false);
                    view.getDeleteContactButton().setEnabled(false);
                    view.getAddContactButton().setEnabled(true);
                } else if (selectedContacts.size() == 1) {
                    view.getUpdateContactButton().setEnabled(true);
                    view.getDeleteContactButton().setEnabled(true);
                    view.getAddContactButton().setEnabled(false);
                } else {
                    view.getDeleteContactButton().setEnabled(true);
                    view.getUpdateContactButton().setEnabled(false);
                    view.getAddContactButton().setEnabled(false);
                }
            }
        });
	}
    
    private void initAddUpdateDeleteHandlers() {
    	view.getAddContactButton().addClickHandler(new ActionContactHomepageHandler(
                dataProvider, view.getSelectionModel(), view.getAddContactButton(), greetingService, ActionType.ADD));
        view.getUpdateContactButton().addClickHandler(new ActionContactHomepageHandler(
                dataProvider, view.getSelectionModel(), view.getUpdateContactButton(), greetingService, ActionType.UPDATE));
        view.getDeleteContactButton().addClickHandler(new ActionContactHomepageHandler(
				dataProvider, view.getSelectionModel(), view.getDeleteContactButton(), greetingService,
				ActionType.DELETE));
	}

	private void initSelectAllHandler() {
		view.getToggleSelectAllButton().addClickHandler(event -> toggleSelectAllContacts());
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
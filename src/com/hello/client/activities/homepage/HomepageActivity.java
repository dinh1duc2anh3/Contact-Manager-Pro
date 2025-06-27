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
    
    private boolean isTableInitialized = false;
	
    public HomepageActivity(ClientFactory clientFactory, Place place) {
		super(clientFactory, place);
		this.place = new HomepagePlace();
		this.greetingService = clientFactory.getGreetingService();
		this.dataProvider = new ListDataProvider<>();
		clientFactory.setHomePageActivity(this); // important
	}

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.view = clientFactory.getHomepageView(); // Assume ClientFactory provides HomepageView
        panel.setWidget(view.asWidget());
        view.setupUI();
        view.setupContactTable(dataProvider);
        bind();
        loadData();
    }
//
//    private void setupUI() {
//        view.getSearchBox().setText("");
//        // add placeholder
//        view.getSearchBox().getElement().setPropertyString("placeholder", "Search by name or phone number...");
//        view.getAddContactButton().setEnabled(true);
//        view.getUpdateContactButton().setEnabled(false);
//        view.getDeleteContactButton().setEnabled(false);
//        setupContactTable();
//    }

//    private void setupContactTable() {
//
//        CellTable<ContactInfo> contactTable = view.getContactTable();
//        
//        view.getPager().setDisplay(view.getContactTable());
//        view.getContactTable().setPageSize(5);
//        
//        MultiSelectionModel<ContactInfo> selectionModel = view.getSelectionModel();
//        contactTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<ContactInfo>createCheckboxManager());
//        contactTable.setKeyboardSelectionPolicy(CellTable.KeyboardSelectionPolicy.ENABLED);
//        
//        dataProvider.addDataDisplay(contactTable);
//        ListHandler<ContactInfo> sortHandler = new ListHandler<>(dataProvider.getList());
//        contactTable.addColumnSortHandler(sortHandler);
//
//        // Column: Checkbox
//        Column<ContactInfo, Boolean> checkColumn = new Column<ContactInfo, Boolean>(new CheckboxCell(true, false)) {
//            @Override
//            public Boolean getValue(ContactInfo object) {
//                return selectionModel.isSelected(object);
//            }
//        };
//       
//
//        // Column: First Name
//        Column<ContactInfo, String> firstNameColumn = new Column<ContactInfo, String>(new ClickableTextCell()) {
//            @Override
//            public String getValue(ContactInfo contact) {
//                return contact.getFirstName();
//            }
//        };
//        
//        
//
//        // Column: Last Name
//        TextColumn<ContactInfo> lastNameColumn = new TextColumn<ContactInfo>() {
//            @Override
//            public String getValue(ContactInfo object) {
//                return object.getLastName();
//            }
//        };
//       
//        
//        // Column: gender
//        TextColumn<ContactInfo> genderColumn = new TextColumn<ContactInfo>() {
//            @Override
//            public String getValue(ContactInfo object) {
//                return object.getGenderStr();
//            }
//        };
//       
//
//        // Column: Phone Number
//        TextColumn<ContactInfo> phoneNumberColumn = new TextColumn<ContactInfo>() {
//            @Override
//            public String getValue(ContactInfo object) {
//                return object.getPhoneNumber();
//            }
//        };
//        
//
//        // Column: Address
//        TextColumn<ContactInfo> addressColumn = new TextColumn<ContactInfo>() {
//            @Override
//            public String getValue(ContactInfo object) {
//                return object.getAddressStr();
//            }
//        };
//        contactTable.addColumn(checkColumn, "CheckBox");
//        contactTable.addColumn(firstNameColumn, "First Name");
//        contactTable.addColumn(lastNameColumn, "Last Name");
//        contactTable.addColumn(genderColumn, "Gender");
//        contactTable.addColumn(phoneNumberColumn, "Phone Number");
//        contactTable.addColumn(addressColumn, "Address");
//        
//        firstNameColumn.setSortable(true);       
//        lastNameColumn.setSortable(true);
//        genderColumn.setSortable(true);
//        phoneNumberColumn.setSortable(true);
//        addressColumn.setSortable(true);
//        
//        sortHandler.setComparator(firstNameColumn, Comparator.comparing(ContactInfo::getFirstName));
//        sortHandler.setComparator(lastNameColumn, Comparator.comparing(ContactInfo::getLastName));
//        sortHandler.setComparator(genderColumn, Comparator.comparing(ContactInfo::getGenderStr));
//        sortHandler.setComparator(phoneNumberColumn, Comparator.comparing(ContactInfo::getPhoneNumber));
//        sortHandler.setComparator(addressColumn, Comparator.comparing(ContactInfo::getAddressStr));
//        
//        firstNameColumn.setFieldUpdater((index, contact, value) -> {
//        	Runnable reloadCallback = this::loadData;
//        	if (selectionModel.getSelectedSet().size() == 1) {
//        		SimplePanel overlay = new SimplePanel();
//                AddUpdateContactActivity addContactActivity = new AddUpdateContactActivity(
//    					greetingService,
//    					dataProvider, 
//    					selectionModel, 
//    					ActionType.UPDATE, 
//    					overlay,
//    					reloadCallback);
//    			addContactActivity.showDialog();
//        	}
//        });
//    }

    @Override
	protected void bind() {
        // Server + Live Search Handler
    	initSearchHandler();

        // Selection Change Handler
        initSelectionModelHandler();

        // Action Handlers
        initAddUpdateDeleteHandlers();
        
        initSelectAllHandler();
        
        initFieldUpdaterHandler();
        
        bindCtrlFToSearchBox(view.getSearchBox().getElement());
        
        setupFilterControls();
    }
    
    private void initFieldUpdaterHandler() {
		// TODO Auto-generated method stub
    	view.getFirstNameColumn().setFieldUpdater((index, contact, value) -> {
    	    if (view.getSelectionModel().getSelectedSet().size() == 1) {
    	        clientFactory.getPlaceController().goTo(
    	            new AddUpdateContactPlace2(ActionType.UPDATE, contact.getPhoneNumber())
    	        );
    	    }
    	});
		
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
    	
    	view.getSearchBox().getTextBox().addKeyDownHandler(new KeyDownHandler() {
    	    @Override
    	    public void onKeyDown(KeyDownEvent event) {
    	        int keyCode = event.getNativeKeyCode();

    	        if (keyCode == KeyCodes.KEY_ENTER) {
    	            GWT.log("Enter pressed – triggering search button click");
    	            view.getSearchButton().click(); 
    	        }
    	    }
    	});
    	
    	view.getClearSearchButton().addClickHandler(event ->{
    		 loadData();
    	});
    	
    	SearchContactHandler searchHandler = new SearchContactHandler(
                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel(), greetingService);
        view.getSearchButton().addClickHandler(searchHandler);
    	
//		// Server Search Handler
//    	ServerSearchContactHandler serverSearchHandler = new ServerSearchContactHandler(
//                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel(), greetingService);
//        view.getServerSearchButton().addClickHandler(serverSearchHandler);
//
//        // Live Search Handler
//        LiveSearchContactHandler liveSearchHandler = new LiveSearchContactHandler(
//                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel());
//        view.getLiveSearchButton().addClickHandler(liveSearchHandler);
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
    	Runnable reloadCallback = this::loadData;
//    	view.getAddContactButton().addClickHandler(new ActionContactHomepageHandler(
//                dataProvider, 
//                view.getSelectionModel(), 
//                view.getAddContactButton(), 
//                greetingService, 
//                ActionType.ADD,
//                reloadCallback));
    	view.getAddContactButton().addClickHandler(event -> {
    	    clientFactory.getPlaceController().goTo(new AddUpdateContactPlace2(ActionType.ADD,null)); // Add mode
    	});
		view.getUpdateContactButton().addClickHandler(event -> {
			Set<ContactInfo> selected = view.getSelectionModel().getSelectedSet();
			ContactInfo contact = selected.iterator().next();
			clientFactory.getPlaceController()
					.goTo(new AddUpdateContactPlace2(ActionType.UPDATE, contact.getPhoneNumber()));
		});
		
       
		view.getDeleteContactButton().addClickHandler(event -> {
			Set<ContactInfo> selected = view.getSelectionModel().getSelectedSet();
			Set<String> phoneNumbers = selected.stream().map(ContactInfo::getPhoneNumber).collect(Collectors.toSet());
			clientFactory.getPlaceController().goTo(new DeleteContactPlace2(phoneNumbers));

		});
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

    private void setupFilterControls() {
    	view.getGenderValueListBox().setValue(null);
    	view.getGenderValueListBox().setAcceptableValues(Arrays.asList(Gender.values()));
    	
    	
    	view.getAddressValueListBox().setAcceptableValues(Arrays.asList(Address.values()));
    	view.getAddressValueListBox().setValue(null);
        
        view.getGenderValueListBox().addValueChangeHandler(e -> applyFilters());
        view.getAddressValueListBox().addValueChangeHandler(e -> applyFilters());
        view.getResetFilterButton().addClickHandler(e -> resetFilters());
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
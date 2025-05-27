package koolsoft.client.view;

import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.ContactInfoCache;
import koolsoft.shared.enums.ActionType;
import koolsoft.shared.handler.ActionContactHomepageHandler;
import koolsoft.shared.handler.LiveSearchContactHandler;
import koolsoft.shared.handler.ServerSearchContactHandler;
import koolsoft.shared.handler.UpdateContactHomepageHandler;

public class HomePage extends Composite {
	interface HomePageUiBinder extends UiBinder<Widget, HomePage> {}
    private static HomePageUiBinder uiBinder = GWT.create(HomePageUiBinder.class);
	
	private GreetingServiceAsync greetingService ;
	
	private Set<ContactInfo> selectedContacts = null;
	@UiField
    
    TextBox searchBox;

    @UiField
    Button liveSearchButton;
    
    @UiField
    Button serverSearchButton;
    
    @UiField
    Element spinner; 

    @UiField
    Button addContactButton;

    @UiField
    Button updateContactButton;

    @UiField
    Button deleteContactButton;

    @UiField
    CellTable<ContactInfo> contactTable;
    
    @UiField
    Label errorLabel;

    private static ListDataProvider<ContactInfo> dataProvider = new ListDataProvider<>();
    
    private MultiSelectionModel<ContactInfo> multiSelectionModel = new MultiSelectionModel<>();

    
    public HomePage(GreetingServiceAsync greetingService) {
    	this.greetingService = greetingService;
//    	initWidget(uiBinder.createAndBindUi(this));
    	Widget widget = uiBinder.createAndBindUi(this);
    	initWidget(widget);

        setupUI();
        setupHandlers();
    }

    private void setupUI() {
        searchBox.setText("");
        
        addContactButton.setEnabled(true);
        updateContactButton.setEnabled(false);
        deleteContactButton.setEnabled(false);

        setupContactTable();
    }
    
	private void setupContactTable() {
        contactTable.setSelectionModel(multiSelectionModel, DefaultSelectionEventManager.<ContactInfo>createCheckboxManager());
        contactTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);


        dataProvider.addDataDisplay(contactTable);

        // Column: Checkbox
        Column<ContactInfo, Boolean> checkColumn = new Column<ContactInfo, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(ContactInfo object) {
                return multiSelectionModel.isSelected(object);
            }
        };
        contactTable.addColumn(checkColumn, "CheckBox");

        // Column: First Name
        TextColumn<ContactInfo> firstNameColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getFirstName();
            }
        };
        contactTable.addColumn(firstNameColumn, "First Name");

        // Column: Last Name
        TextColumn<ContactInfo> lastNameColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getLastName();
            }
        };
        contactTable.addColumn(lastNameColumn, "Last Name");

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
                return object.getAddress();
            }
        };
        contactTable.addColumn(addressColumn, "Address");
        
        greetingService.getAllContactInfos(new AsyncCallback<List<ContactInfo>>() {
			public void onFailure(Throwable caught) {
				GWT.log("Error: initially getting all contact info ");
				GWT.log("Error occurred while fetching contacts", caught);
			}

			public void onSuccess(List<ContactInfo> result) {
				GWT.log("Success: initially getting all contact info from server");
				dataProvider.getList().clear();
				dataProvider.getList().addAll(result);
				dataProvider.refresh();
				ContactInfoCache.setCurrentContacts(result);
			}
		});
    }

	
	private void setupHandlers() {
		initSearchHandler();
		initSelectionModelHandler();
		initAddUpdateDeleteHandlers();
	
	}
	
	private void initSearchHandler() {		
		//Add a handler to  server search button 
		ServerSearchContactHandler serverSearchContactHandler = new ServerSearchContactHandler(dataProvider ,spinner
				 ,  searchBox, errorLabel, greetingService);
		serverSearchButton.addClickHandler(serverSearchContactHandler);

		// Add a handler to live search button + search field
		LiveSearchContactHandler liveSearchContactHandler = new LiveSearchContactHandler(dataProvider ,spinner
				 ,  searchBox, errorLabel);
		liveSearchButton.addClickHandler(liveSearchContactHandler);
	}
	
	private void initSelectionModelHandler() {
		multiSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		    @Override
		    public void onSelectionChange(SelectionChangeEvent event) {
		        selectedContacts = multiSelectionModel.getSelectedSet();
//		        GWT.log("number of contacts being selected is: " + selectedContacts.size());
		        if (selectedContacts.size() == 0) {
		        	updateContactButton.setEnabled(false);
		        	deleteContactButton.setEnabled(false);
		        	addContactButton.setEnabled(true);
		        }
		        else if (selectedContacts.size() == 1) {
		        	updateContactButton.setEnabled(true);
		        	deleteContactButton.setEnabled(true);
		        	addContactButton.setEnabled(false);
		        }
		        else {
		        	deleteContactButton.setEnabled(true);
		        	updateContactButton.setEnabled(false);
		        	addContactButton.setEnabled(false);
		        }
		        
		    }
		});
	}
	
	private void initAddUpdateDeleteHandlers() {
		ActionContactHomepageHandler addContactHomepageHandler = new ActionContactHomepageHandler(dataProvider, multiSelectionModel, addContactButton,
				greetingService, ActionType.ADD);
		addContactButton.addClickHandler(addContactHomepageHandler );

		ActionContactHomepageHandler updateContactHomepageHandler = new ActionContactHomepageHandler(dataProvider, multiSelectionModel, updateContactButton, 
				greetingService, ActionType.UPDATE);
		updateContactButton.addClickHandler(updateContactHomepageHandler );

		ActionContactHomepageHandler deleteContactHomepageHandler = new ActionContactHomepageHandler(dataProvider, multiSelectionModel, deleteContactButton, 
				greetingService, ActionType.DELETE);
		deleteContactButton.addClickHandler(deleteContactHomepageHandler );
	}
	
	
}
	

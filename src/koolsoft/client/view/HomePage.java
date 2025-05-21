package koolsoft.client.view;

import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import koolsoft.client.service.GreetingService;
import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.shared.ContactInfo;
import koolsoft.shared.FieldVerifier;
import koolsoft.shared.enums.ActionType;
import koolsoft.shared.enums.SearchValidationResult;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.exception.ContactNoneExistsException;

public class HomePage extends Composite {
	interface HomePageUiBinder extends UiBinder<Widget, HomePage> {}
    private static HomePageUiBinder uiBinder = GWT.create(HomePageUiBinder.class);
	
	private GreetingServiceAsync greetingService = null;
	
	private Set<ContactInfo> selectedContacts = null;
	private ContactInfo selectedContact = null;
	
    @UiField
    TextBox searchBox;

    @UiField
    Button searchButton;
    
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
    	
    	initWidget(uiBinder.createAndBindUi(this));
    	this.greetingService = greetingService;

        setupUI();
        setupHandlers();
    }

    private void setupUI() {
    	
    	// Khởi tạo các thành phần


        // SearchBox
        searchBox.setText("");
        addContactButton.setEnabled(true);

        // Update Button
        updateContactButton.setEnabled(false);

        // Delete Button
        deleteContactButton.setEnabled(false);

//       CellTable setup
        setupContactTable();
    }
    
    // Then use showSpinner(true) when starting an operation
    // And showSpinner(false) when the operation completes
       public void showSpinner(boolean show) {
           // Only change display property, not visibility
           spinner.getStyle().setDisplay(show ? Display.BLOCK : Display.NONE);
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
			}
		});
    }

	
	private void setupHandlers() {
		initSearchHandler();
		initSelectionModelHandler();
		initAddUpdateDeleteHandlers();
	
	}
	
	private void initSearchHandler() {
		class SearchContactHandler implements ClickHandler, KeyUpHandler {

			public void onClick(ClickEvent event) {
				showSpinner(true);
				// Add it to the root panel.
				String keyword = searchBox.getText();
				GWT.log("send keyword: "+keyword+ " to server ");
				sendNameToServer(keyword);
			}

			public void onKeyUp(KeyUpEvent event) {
				showSpinner(true);
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String keyword = searchBox.getText();
					GWT.log("send keyword: "+keyword+ " to server ");
					sendNameToServer(keyword);
				}
			}

			private void sendNameToServer(String keyword) {
				// First, we validate the input.
				errorLabel.setText("");
				
				SearchValidationResult fieldVerifier = FieldVerifier.isValidSearchKeyword(keyword);
				
				//ko du 4 ki tu 
				if (fieldVerifier.equals(SearchValidationResult.TOO_SHORT)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}
				if (fieldVerifier.equals(SearchValidationResult.EMPTY)) {
					// display all contact
					greetingService.getAllContactInfos( new AsyncCallback<List<ContactInfo>>() {
						public void onFailure(Throwable caught) {
							showSpinner(false);
							GWT.log("Error: get all contact from firstname from server");
							Window.alert("Error fetching contacts by all name");
						}

						public void onSuccess(List<ContactInfo> result) {
							showSpinner(false);
							GWT.log("Success: get all contact from firstname from server");
							dataProvider.getList().clear();
							dataProvider.getList().addAll(result);
							dataProvider.refresh();							
						}
					});							
					return;
				}
				if (fieldVerifier.equals(SearchValidationResult.VALID)) {
					//display specific contact
					greetingService.getContactInfosByFirstName(keyword, new AsyncCallback<List<ContactInfo>>() {
						
						public void onFailure(Throwable caught) {
							showSpinner(false);
							if (caught instanceof ContactNoneExistsException) {
								Window.alert("Error: " + caught.getMessage());
							} else {
								GWT.log("Error: unexpected error", caught);
								Window.alert("An unexpected error occurred while fetching contacts by first name" + keyword);
							}
						}

						public void onSuccess(List<ContactInfo> result) {
							showSpinner(false);
							GWT.log("Success: get suitable contact from firstname: "+keyword+" from server is success");
							dataProvider.getList().clear();
							dataProvider.getList().addAll(result);
							dataProvider.refresh();													
						}
					});
				}		

			}
		}
		
		//Add a handler to search button + search field
		SearchContactHandler searchContactHandler = new SearchContactHandler();
		searchButton.addClickHandler(searchContactHandler);
		searchBox.addKeyUpHandler(searchContactHandler);
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
		        	selectedContact = null;
		        }
		        else if (selectedContacts.size() == 1) {
		        	updateContactButton.setEnabled(true);
		        	deleteContactButton.setEnabled(true);
		        	addContactButton.setEnabled(false);
		        	selectedContact = selectedContacts.iterator().next();
		        }
		        else {
		        	deleteContactButton.setEnabled(true);
		        	updateContactButton.setEnabled(false);
		        	addContactButton.setEnabled(false);
		        	selectedContact = null;
		        }
		        
		    }
		});
	}
	
	private void initAddUpdateDeleteHandlers() {
		// Add a handler to open the add info DialogBox
		addContactButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				// Tạo overlay element
				SimplePanel overlay = new SimplePanel();
				overlay.getElement().setId("dialogOverlay");
				overlay.setStyleName("modal-overlay");

				// Thêm overlay vào RootPanel trước khi hiển thị dialog
				if (RootPanel.get().getWidgetIndex(overlay) == -1) {
					RootPanel.get().add(overlay);
				}

				// Create the dialog
				AddUpdateContactInfoDialog addContactInfoDialog = new AddUpdateContactInfoDialog(greetingService,
						addContactButton, dataProvider, selectedContact, multiSelectionModel, ActionType.ADD, overlay);
				addContactInfoDialog.showDialog();

				addContactButton.setEnabled(false);

			}
		});

		updateContactButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Tạo overlay element
				SimplePanel overlay = new SimplePanel();
				overlay.getElement().setId("dialogOverlay");
				overlay.setStyleName("modal-overlay");

				AddUpdateContactInfoDialog updateContactInfoDialog = new AddUpdateContactInfoDialog(greetingService,
						updateContactButton, dataProvider, selectedContact, multiSelectionModel, ActionType.UPDATE,
						overlay);
				updateContactInfoDialog.showDialog();
			}
		});

		deleteContactButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Tạo overlay element
				SimplePanel overlay = new SimplePanel();
				overlay.getElement().setId("dialogOverlay");
				overlay.setStyleName("modal-overlay");
				
				// Create the dialog	
				DeleteContactInfoDialog deleteContactInfoDialog = new DeleteContactInfoDialog(greetingService, deleteContactButton,
						dataProvider,selectedContacts, multiSelectionModel, overlay);
				
				deleteContactInfoDialog.showDialog();


			}
		});
	}
	
	
}
	

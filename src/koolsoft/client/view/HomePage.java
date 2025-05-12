package koolsoft.client.view;

import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

import koolsoft.client.enums.ActionType;
import koolsoft.client.service.GreetingService;
import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.shared.ContactInfoDTO;
import koolsoft.shared.FieldVerifier;

public class HomePage extends Composite {
	interface HomePageUiBinder extends UiBinder<Widget, HomePage> {}
    private static HomePageUiBinder uiBinder = GWT.create(HomePageUiBinder.class);
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	private Set<ContactInfoDTO> selectedContacts = null;
	private ContactInfoDTO selectedContact = null;
	
    @UiField
    TextBox searchBox;

    @UiField
    Button searchButton;

    @UiField
    Button addContactButton;

    @UiField
    Button updateContactButton;

    @UiField
    Button deleteContactButton;

    @UiField
    CellTable<ContactInfoDTO> contactTable;
    
    @UiField
    Label errorLabel;

    private static ListDataProvider<ContactInfoDTO> dataProvider = new ListDataProvider<>();
    
    private MultiSelectionModel<ContactInfoDTO> multiSelectionModel = new MultiSelectionModel<>();

    
    public HomePage() {
    	initWidget(uiBinder.createAndBindUi(this));

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
    
    private void setupContactTable() {
        contactTable.setSelectionModel(multiSelectionModel, DefaultSelectionEventManager.<ContactInfoDTO>createCheckboxManager());
        contactTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);


        dataProvider.addDataDisplay(contactTable);

        // Column: Checkbox
        Column<ContactInfoDTO, Boolean> checkColumn = new Column<ContactInfoDTO, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(ContactInfoDTO object) {
                return multiSelectionModel.isSelected(object);
            }
        };
        contactTable.addColumn(checkColumn, "CheckBox");

        // Column: First Name
        TextColumn<ContactInfoDTO> firstNameColumn = new TextColumn<ContactInfoDTO>() {
            @Override
            public String getValue(ContactInfoDTO object) {
                return object.getFirstName();
            }
        };
        contactTable.addColumn(firstNameColumn, "First Name");

        // Column: Last Name
        TextColumn<ContactInfoDTO> lastNameColumn = new TextColumn<ContactInfoDTO>() {
            @Override
            public String getValue(ContactInfoDTO object) {
                return object.getLastName();
            }
        };
        contactTable.addColumn(lastNameColumn, "Last Name");

        // Column: Phone Number
        TextColumn<ContactInfoDTO> phoneNumberColumn = new TextColumn<ContactInfoDTO>() {
            @Override
            public String getValue(ContactInfoDTO object) {
                return object.getPhoneNumber();
            }
        };
        contactTable.addColumn(phoneNumberColumn, "Phone Number");

        // Column: Address
        TextColumn<ContactInfoDTO> addressColumn = new TextColumn<ContactInfoDTO>() {
            @Override
            public String getValue(ContactInfoDTO object) {
                return object.getAddress();
            }
        };
        contactTable.addColumn(addressColumn, "Address");
        
        greetingService.getAllContactInfos(new AsyncCallback<List<ContactInfoDTO>>() {
			public void onFailure(Throwable caught) {
				GWT.log("Error: initially getting all contact info ");
			}

			public void onSuccess(List<ContactInfoDTO> result) {
				GWT.log("Success: initially getting all contact info from server");
				dataProvider.getList().clear();
				dataProvider.getList().addAll(result);
				dataProvider.refresh();
			}
		});
    }

	
	private void setupHandlers() {
		class SearchContactHandler implements ClickHandler, KeyUpHandler {

			public void onClick(ClickEvent event) {
				// Add it to the root panel.
				String keyword = searchBox.getText();
				GWT.log("send keyword: "+keyword+ " to server ");
				sendNameToServer(keyword);
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					String keyword = searchBox.getText();
					GWT.log("send keyword: "+keyword+ " to server ");
					sendNameToServer(keyword);
				}
			}

			private void sendNameToServer(String keyword) {
				// First, we validate the input.
				errorLabel.setText("");
				
				int fieldVerifier = FieldVerifier.isValidSearchKeyword(keyword);
				
				//ko du 4 ki tu 
				if (fieldVerifier == 0 ) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}
				if (fieldVerifier == -1 ) {
					// display all contact
					greetingService.getAllContactInfos( new AsyncCallback<List<ContactInfoDTO>>() {
						public void onFailure(Throwable caught) {
							GWT.log("Error: get all contact from firstname from server");
							Window.alert("Error fetching contacts by all name");
						}

						public void onSuccess(List<ContactInfoDTO> result) {
							GWT.log("Success: get all contact from firstname from server");
							dataProvider.getList().clear();
							dataProvider.getList().addAll(result);
							dataProvider.refresh();							
						}
					});							
					return;
				}
				if (fieldVerifier == 1) {
					//display specific contact
					greetingService.getContactInfosByFirstName(keyword, new AsyncCallback<List<ContactInfoDTO>>() {
						public void onFailure(Throwable caught) {
							GWT.log("Error: get specific contact from firstname: "+keyword+" from server");
							Window.alert("Error fetching contacts by first name");
						}

						public void onSuccess(List<ContactInfoDTO> result) {
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
		

		
		//Add a handler to open the add info DialogBox
    	addContactButton.addClickHandler(new ClickHandler() {
    		@Override
    		public void onClick(ClickEvent event) {
					// Create the dialog
    			AddUpdateContactInfoDialog addContactInfoDialog = new AddUpdateContactInfoDialog(greetingService, addContactButton,
						dataProvider,selectedContact, multiSelectionModel, ActionType.ADD);
		        	addContactInfoDialog.showDialog();
					
					addContactButton.setEnabled(false);
					
				}
			});
	
    
		updateContactButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				AddUpdateContactInfoDialog updateContactInfoDialog = new AddUpdateContactInfoDialog(greetingService, updateContactButton,
						dataProvider,selectedContact, multiSelectionModel , ActionType.UPDATE);
				updateContactInfoDialog.showDialog();
			}
		});
		
		deleteContactButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("1 - pressed delete button at homepage");
				// Create the dialog	
				DeleteContactInfoDialog deleteContactInfoDialog = new DeleteContactInfoDialog(greetingService, deleteContactButton,
						dataProvider,selectedContacts, multiSelectionModel);
				deleteContactInfoDialog.showDialog();

				deleteContactButton.setEnabled(false);

			}
		});
		
		
	
	}
}

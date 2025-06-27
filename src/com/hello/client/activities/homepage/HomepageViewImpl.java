package com.hello.client.activities.homepage;

import java.util.Comparator;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.client.activities.addupdate2.AddUpdateContactPlace2;
import com.hello.client.activities.addupdateDialog.AddUpdateContactActivity;
import com.hello.shared.enums.ActionType;
import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.model.ContactInfo;

public class HomepageViewImpl extends Composite implements HomepageView {

    interface HomepageViewImplUiBinder extends UiBinder<Widget, HomepageViewImpl> {
    }

    private static HomepageViewImplUiBinder uiBinder = GWT.create(HomepageViewImplUiBinder.class);

    
    @UiField SuggestBox searchBox;
    
    @UiField Button clearSearchButton;

    @UiField Button searchButton;

    @UiField Element spinner;

    @UiField Button addContactButton;

    @UiField Button updateContactButton;

    @UiField Button deleteContactButton;
    
    @UiField Button toggleSelectAllButton;

    @UiField Label errorLabel;
    
    @UiField ValueListBox<Gender> genderValueListBox;

    @UiField ValueListBox<Address> addressValueListBox;

    @UiField Button resetFilterBtn;

    @UiField CellTable<ContactInfo> contactTable;
    
    @UiField SimplePager pager;
    
    private Column<ContactInfo, String> firstNameColumn;

    private final MultiSelectionModel<ContactInfo> selectionModel = new MultiSelectionModel<>();

    public HomepageViewImpl() {
    	genderValueListBox = new ValueListBox<>(new AbstractRenderer<Gender>() {
            @Override
            public String render(Gender gender) {
                return gender == null ? "Khác" : gender.toString();
            }
        });
    	
        addressValueListBox = new ValueListBox<>(new AbstractRenderer<Address>() {
            @Override
            public String render(Address address) {
                return address == null ? "Tất cả" : address.toString();
            }
        });
        
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public void setupUI() {
        searchBox.setText("");
        searchBox.getElement().setPropertyString("placeholder", "Search by name or phone number...");
        addContactButton.setEnabled(true);
        updateContactButton.setEnabled(false);
        deleteContactButton.setEnabled(false);
    }
    
    @Override
    public void setupContactTable(ListDataProvider<ContactInfo> dataProvider) {
        
        pager.setDisplay(contactTable);
        contactTable.setPageSize(5);
        contactTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<ContactInfo>createCheckboxManager());
        contactTable.setKeyboardSelectionPolicy(CellTable.KeyboardSelectionPolicy.ENABLED);

        dataProvider.addDataDisplay(contactTable);
        ListHandler<ContactInfo> sortHandler = new ListHandler<>(dataProvider.getList());
        contactTable.addColumnSortHandler(sortHandler);

        // Column: Checkbox
        Column<ContactInfo, Boolean> checkColumn = new Column<ContactInfo, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(ContactInfo object) {
                return selectionModel.isSelected(object);
            }
        };

        // Column: First Name
        firstNameColumn = new Column<ContactInfo, String>(new ClickableTextCell()) {
            @Override
            public String getValue(ContactInfo contact) {
                return contact.getFirstName();
            }
            
        };

        // Column: Last Name
        TextColumn<ContactInfo> lastNameColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getLastName();
            }
        };

        // Column: Gender
        TextColumn<ContactInfo> genderColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getGenderStr();
            }
        };

        // Column: Phone Number
        TextColumn<ContactInfo> phoneNumberColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getPhoneNumber();
            }
        };

        // Column: Address
        TextColumn<ContactInfo> addressColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo object) {
                return object.getAddressStr();
            }
        };

        contactTable.addColumn(checkColumn, "CheckBox");
        contactTable.addColumn(firstNameColumn, "First Name");
        contactTable.addColumn(lastNameColumn, "Last Name");
        contactTable.addColumn(genderColumn, "Gender");
        contactTable.addColumn(phoneNumberColumn, "Phone Number");
        contactTable.addColumn(addressColumn, "Address");

        firstNameColumn.setSortable(true);
        lastNameColumn.setSortable(true);
        genderColumn.setSortable(true);
        phoneNumberColumn.setSortable(true);
        addressColumn.setSortable(true);

        sortHandler.setComparator(firstNameColumn, Comparator.comparing(ContactInfo::getFirstName));
        sortHandler.setComparator(lastNameColumn, Comparator.comparing(ContactInfo::getLastName));
        sortHandler.setComparator(genderColumn, Comparator.comparing(ContactInfo::getGenderStr));
        sortHandler.setComparator(phoneNumberColumn, Comparator.comparing(ContactInfo::getPhoneNumber));
        sortHandler.setComparator(addressColumn, Comparator.comparing(ContactInfo::getAddressStr));
        
        
    }
    
    @Override
    public Column<ContactInfo, String> getFirstNameColumn() {
        return firstNameColumn;
    }

	@Override
    public SuggestBox getSearchBox() {
        return searchBox;
    }

	@Override
    public Button getClearSearchButton() {
		return clearSearchButton;
	}

	@Override
    public Button getSearchButton() {
        return searchButton;
    }


    @Override
    public Button getAddContactButton() {
        return addContactButton;
    }

    @Override
    public Button getUpdateContactButton() {
        return updateContactButton;
    }

    @Override
    public Button getDeleteContactButton() {
        return deleteContactButton;
    }

    @Override
    public Button getToggleSelectAllButton() {
		return toggleSelectAllButton;
	}

	@Override
    public Label getErrorLabel() {
        return errorLabel;
    }
	
	@Override
	public ValueListBox<Gender> getGenderValueListBox() {
	    return genderValueListBox;
	}

	@Override
	public ValueListBox<Address> getAddressValueListBox() {
	    return addressValueListBox;
	}

	@Override
	public HasClickHandlers getResetFilterButton() {
	    return resetFilterBtn;
	}

    @Override
    public CellTable<ContactInfo> getContactTable() {
        return contactTable;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }
    
    @Override
    public MultiSelectionModel<ContactInfo> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void setSpinnerVisible(boolean visible) {
        spinner.getStyle().setProperty("display", visible ? "block" : "none");
    }

    @Override
    public Widget asWidget() {
        return this;
    }

	@Override
	public Element getSpinner() {
		return spinner;
	}
	
	
}
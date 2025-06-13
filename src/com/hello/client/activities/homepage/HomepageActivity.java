package com.hello.client.activities.homepage;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.MultiSelectionModel;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.ClientFactory;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.enums.ActionType;
import com.hello.shared.handler.ActionContactHomepageHandler;
import com.hello.shared.handler.LiveSearchContactHandler;
import com.hello.shared.handler.ServerSearchContactHandler;

import java.util.List;
import java.util.Set;

public class HomepageActivity extends MGWTAbstractActivity {

    private final ClientFactory clientFactory; // Assume ClientFactory provides dependencies
    private final HomepagePlace place;
    private HomepageView view;
    private final GreetingServiceAsync greetingService;
    private ListDataProvider<ContactInfo> dataProvider;
    private Set<ContactInfo> selectedContacts;

    public HomepageActivity(ClientFactory clientFactory, HomepagePlace place) {
        this.clientFactory = clientFactory;
        this.place = place;
        this.greetingService = clientFactory.getGreetingService(); // Assume ClientFactory provides this
        this.dataProvider = new ListDataProvider<>();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.view = clientFactory.getHomepageView(); // Assume ClientFactory provides HomepageView
        panel.setWidget(view.asWidget());
        setupUI();
        setupHandlers();
        loadData();
    }

    private void setupUI() {
        view.getSearchBox().setText("");
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
    }

    private void setupHandlers() {
        // Server Search Handler
        ServerSearchContactHandler serverSearchHandler = new ServerSearchContactHandler(
                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel(), greetingService);
        view.getServerSearchButton().addClickHandler(serverSearchHandler);

        // Live Search Handler
        LiveSearchContactHandler liveSearchHandler = new LiveSearchContactHandler(
                dataProvider, view.getSpinner(), view.getSearchBox(), view.getErrorLabel());
        view.getLiveSearchButton().addClickHandler(liveSearchHandler);

        // Selection Change Handler
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

        // Action Handlers
        view.getAddContactButton().addClickHandler(new ActionContactHomepageHandler(
                dataProvider, view.getSelectionModel(), view.getAddContactButton(), greetingService, ActionType.ADD));
        view.getUpdateContactButton().addClickHandler(new ActionContactHomepageHandler(
                dataProvider, view.getSelectionModel(), view.getUpdateContactButton(), greetingService, ActionType.UPDATE));
        view.getDeleteContactButton().addClickHandler(new ActionContactHomepageHandler(
                dataProvider, view.getSelectionModel(), view.getDeleteContactButton(), greetingService, ActionType.DELETE));
    }

    private void loadData() {
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
            }
        });
    }
}
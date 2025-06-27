package com.hello.client.activities.homepage;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.client.activities.ClientFactory;
import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.model.ContactInfo;

public interface HomepageView extends IsWidget {
	SuggestBox getSearchBox();
    Button getClearSearchButton();
    Button getSearchButton();
	Button getAddContactButton();
    Button getUpdateContactButton();
    Button getDeleteContactButton();
	Button getToggleSelectAllButton();
	Label getErrorLabel();
	ValueListBox<Gender> getGenderValueListBox();
	ValueListBox<Address> getAddressValueListBox();
	HasClickHandlers getResetFilterButton();
    CellTable<ContactInfo> getContactTable();
    SimplePager getPager();
	MultiSelectionModel<ContactInfo> getSelectionModel();
    Element getSpinner();
	void setSpinnerVisible(boolean visible);
	
	// New methods for UI setup and event handling
    void setupUI();
    void setupContactTable(ListDataProvider<ContactInfo> dataProvider);
	Column<ContactInfo, String> getFirstNameColumn();
}
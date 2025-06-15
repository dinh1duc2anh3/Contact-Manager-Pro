package com.hello.client.activities.homepage;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.shared.model.ContactInfo;

public interface HomepageView extends IsWidget {
    ScrollPanel getTableScrollPanel();
	SuggestBox getSearchBox();
    Button getLiveSearchButton();
    Button getServerSearchButton();
    Button getAddContactButton();
    Button getUpdateContactButton();
    Button getDeleteContactButton();
	Button getToggleSelectAllButton();
	Label getErrorLabel();
    CellTable<ContactInfo> getContactTable();
    MultiSelectionModel<ContactInfo> getSelectionModel();
    Element getSpinner();
	void setSpinnerVisible(boolean visible);
}
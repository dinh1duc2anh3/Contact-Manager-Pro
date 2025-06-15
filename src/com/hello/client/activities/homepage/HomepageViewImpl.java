package com.hello.client.activities.homepage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.hello.shared.model.ContactInfo;

public class HomepageViewImpl extends Composite implements HomepageView {

    interface HomepageViewImplUiBinder extends UiBinder<Widget, HomepageViewImpl> {
    }

    private static HomepageViewImplUiBinder uiBinder = GWT.create(HomepageViewImplUiBinder.class);

    @UiField ScrollPanel tableScrollPanel;
    
    @UiField SuggestBox searchBox;

    @UiField Button liveSearchButton;

    @UiField Button serverSearchButton;

    @UiField Element spinner;

    @UiField Button addContactButton;

    @UiField Button updateContactButton;

    @UiField Button deleteContactButton;
    
    @UiField Button toggleSelectAllButton;

    @UiField Label errorLabel;

    @UiField CellTable<ContactInfo> contactTable;

    private final MultiSelectionModel<ContactInfo> selectionModel = new MultiSelectionModel<>();

    public HomepageViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
	public ScrollPanel getTableScrollPanel() {
		return tableScrollPanel;
	}

	@Override
    public SuggestBox getSearchBox() {
        return searchBox;
    }

    @Override
    public Button getLiveSearchButton() {
        return liveSearchButton;
    }

    @Override
    public Button getServerSearchButton() {
        return serverSearchButton;
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
    public CellTable<ContactInfo> getContactTable() {
        return contactTable;
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
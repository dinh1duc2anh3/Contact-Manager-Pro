package com.hello.shared.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.verifier.FieldVerifier;
import com.hello.shared.enums.SearchValidationResult;

public class LiveSearchContactHandler implements ClickHandler{
	private  ListDataProvider<ContactInfo> dataProvider = new ListDataProvider<>();
	private Element spinner; 
	private TextBox searchBox;
	private Label errorLabel;
	
	public LiveSearchContactHandler(ListDataProvider<ContactInfo> dataProvider ,Element spinner
			 , TextBox searchBox, Label errorLabel) {
		this.dataProvider = dataProvider;
		this.spinner = spinner;
		this.searchBox = searchBox; 
		this.errorLabel = errorLabel;
		// TODO Auto-generated constructor stub
	}
	
	public void showSpinner(boolean show) {
        // Only change display property, not visibility
        spinner.getStyle().setDisplay(show ? Display.BLOCK : Display.NONE);
    }
	
	@Override
	public void onClick(ClickEvent event) {
		showSpinner(true);
		// Add it to the root panel.
		String keyword = searchBox.getText().trim();
		GWT.log("send keyword: "+keyword+ " for live search ");
		sendNameToLiveSearch(keyword);
	}
	
	private void sendNameToLiveSearch(String keyword) {
		// First, we validate the input.
		errorLabel.setText("");
		
		SearchValidationResult fieldVerifier = FieldVerifier.isValidSearchKeyword(keyword);
		
		//ko du 4 ki tu 
		if (fieldVerifier.equals(SearchValidationResult.TOO_SHORT)) {
			errorLabel.setText("Please enter at least four characters");
			return;
		}
		
		updateDisplayContacts(keyword,fieldVerifier);
		
		
		showSpinner(false);
	}

	private void updateDisplayContacts(String keyword,SearchValidationResult fieldVerifier) {
		
		//clear data in dataprovider
		dataProvider.getList().clear();
		
		if (fieldVerifier.equals(SearchValidationResult.EMPTY)) {
			// display all contact
			GWT.log("Success: live search : showing all contacts");
		    dataProvider.getList().addAll(ContactInfoCache.getCurrentContacts());
					
		}
		if (fieldVerifier.equals(SearchValidationResult.VALID)) {
			//search with criteria as firstname, fullname, phonenumber
			List<ContactInfo> filterContactList = new ArrayList<>();

			for (ContactInfo contact : ContactInfoCache.getCurrentContacts()) {
				if (keyword.equalsIgnoreCase(contact.getFirstName())) {
					filterContactList.add(contact);
				}
			}
			GWT.log("Success: showing " + filterContactList.size() + " filtered contacts");
			dataProvider.getList().addAll(filterContactList);
		}		
		dataProvider.refresh();
	}
	
}
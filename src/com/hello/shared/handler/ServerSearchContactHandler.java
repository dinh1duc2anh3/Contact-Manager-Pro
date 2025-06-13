package com.hello.shared.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;

import com.hello.client.GreetingServiceAsync;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.verifier.FieldVerifier;
import com.hello.shared.enums.SearchValidationResult;
import com.hello.shared.exception.ContactNoneExistsException;

public class ServerSearchContactHandler implements ClickHandler {

	private ListDataProvider<ContactInfo> dataProvider = new ListDataProvider<>();
	private Element spinner; 
	private TextBox searchBox;
	private Label errorLabel;
	
	private GreetingServiceAsync greetingService = null;
	
	public ServerSearchContactHandler(ListDataProvider<ContactInfo> dataProvider ,Element spinner
			 , TextBox searchBox, Label errorLabel,GreetingServiceAsync greetingService ) {
		this.dataProvider = dataProvider;
		this.spinner = spinner;
		this.searchBox = searchBox; 
		this.errorLabel = errorLabel;
		this.greetingService = greetingService;
	}
	
	public void showSpinner(boolean show) {
        // Only change display property, not visibility
        spinner.getStyle().setDisplay(show ? Display.BLOCK : Display.NONE);
    }
	
	public void onClick(ClickEvent event) {
		showSpinner(true);
		// Add it to the root panel.
		String keyword = searchBox.getText().trim();
		GWT.log("send keyword: "+keyword+ " for server search ");
		sendNameToServerSearch(keyword);
	}

	private void sendNameToServerSearch(String keyword) {
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
					GWT.log("Error: get all contact from server");
					Window.alert("Error fetching contacts by all name");
				}

				public void onSuccess(List<ContactInfo> result) {
					handleResult(result, "getting all from server");
					showSpinner(false);				
				}
			});							
			return; 
		}
		if (fieldVerifier.equals(SearchValidationResult.VALID)) {
			//search with criteria as firstname, fullname, phonenumber
			searchWithFallback(keyword);
		}		

	}
	
	
	private void searchWithFallback( String keyword) {
	    showSpinner(true);
	    searchByFirstName(keyword);
	}
	
	private void searchByFirstName( String keyword) {
	    greetingService.getContactInfosByFirstName(keyword, new AsyncCallback<List<ContactInfo>>() {
	        public void onFailure(Throwable caught) {
	        	if (caught instanceof ContactNoneExistsException) {
					Window.alert("Error: " + caught.getMessage());
				} else {
					GWT.log("Error: unexpected error", caught);
				}
	            searchByFullName(keyword); // fallback
	        }

	        public void onSuccess(List<ContactInfo> result) {
	            if (result != null && !result.isEmpty()) {
	                handleResult(result, "first name");
	            } else {
	                searchByFullName(keyword); // fallback
	            }
	        }
	    });
	}
	private void searchByFullName( String keyword) {
	    greetingService.getContactInfosByFullName(keyword, new AsyncCallback<List<ContactInfo>>() {
	        public void onFailure(Throwable caught) {
	        	if (caught instanceof ContactNoneExistsException) {
					Window.alert("Error: " + caught.getMessage());
				} else {
					GWT.log("Error: unexpected error", caught);
				}
	            searchByPhoneNumber(keyword); // fallback
	        }

	        public void onSuccess(List<ContactInfo> result) {
	            if (result != null && !result.isEmpty()) {
	                handleResult(result, "full name");
	            } else {
	                searchByPhoneNumber(keyword); // fallback
	            }
	        }
	    });
	}
	private void searchByPhoneNumber( String keyword) {
	    greetingService.getContactInfosByPhoneNumber(keyword, new AsyncCallback<ContactInfo>() {
	        public void onFailure(Throwable caught) {
	            showSpinner(false);
	            GWT.log("Error searching by phone number", caught);
	            Window.alert("No contacts found.");
	        }

	        public void onSuccess(ContactInfo result) {
	            showSpinner(false);
	            if (result != null) {
	                List<ContactInfo> list = new ArrayList<>();
	                list.add(result);
	                handleResult(list, "phone number");
	            } else {
	                Window.alert("No contacts found.");
	            }
	        }
	    });
	}
	
	private void handleResult(List<ContactInfo> contacts, String criteria) {
	    GWT.log("Success: found contacts by " + criteria);
	    dataProvider.getList().clear();
	    dataProvider.getList().addAll(contacts);
	    dataProvider.refresh();
	    //cache for live search
	    ContactInfoCache.setCurrentContacts(contacts);
	    showSpinner(false);
	}
}


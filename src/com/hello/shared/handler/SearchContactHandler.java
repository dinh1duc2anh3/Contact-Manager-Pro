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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.view.client.ListDataProvider;
import com.hello.client.GreetingServiceAsync;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.enums.SearchValidationResult;
import com.hello.shared.exception.ContactNoneExistsException;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.verifier.FieldVerifier;

public class SearchContactHandler implements ClickHandler {
	private ListDataProvider<ContactInfo> dataProvider;
    private Element spinner;
    private SuggestBox searchBox;
    private Label errorLabel;
    private GreetingServiceAsync greetingService;

    public SearchContactHandler(
    		ListDataProvider<ContactInfo> dataProvider, 
    		Element spinner,    
    		SuggestBox searchBox, 
    		Label errorLabel,
    		GreetingServiceAsync greetingService) {
        this.dataProvider = dataProvider;
        this.spinner = spinner;
        this.searchBox = searchBox;
        this.errorLabel = errorLabel;
        this.greetingService = greetingService;
    }

    private void showSpinner(boolean show) {
        spinner.getStyle().setDisplay(show ? Display.BLOCK : Display.NONE);
    }

    @Override
    public void onClick(ClickEvent event) {
        String keyword = searchBox.getText().trim();
        errorLabel.setText("");
        GWT.log("Search keyword: " + keyword);

        SearchValidationResult validation = FieldVerifier.isValidSearchKeyword(keyword);

        if (validation == SearchValidationResult.EMPTY) {
            handleResult(ContactInfoCache.getCurrentContacts(), "all (local)");
            return;
        }

        if (validation == SearchValidationResult.TOO_SHORT) {
            errorLabel.setText("Please enter at least four characters");
            return;
        }

        // VALID keyword
        showSpinner(true);
        List<ContactInfo> localMatches = filterLocal(keyword);
        if (!localMatches.isEmpty()) {
            handleResult(localMatches, "local");
            return;
        }

        // fallback to server
        searchServer(keyword);
    }

    private List<ContactInfo> filterLocal(String keyword) {
        List<ContactInfo> result = new ArrayList<>();
        String lower = keyword.toLowerCase();

        for (ContactInfo contact : ContactInfoCache.getCurrentContacts()) {
            boolean matchFirst = contact.getFirstName() != null && contact.getFirstName().toLowerCase().contains(lower);
            boolean matchFull = contact.getFullName() != null && contact.getFullName().toLowerCase().contains(lower);
            boolean matchPhone = contact.getPhoneNumber() != null && contact.getPhoneNumber().contains(keyword); // keep phone exact

            if (matchFirst || matchFull || matchPhone) result.add(contact);
        }

        return result;
    }

    private void searchServer(String keyword) {
        searchByFirstName(keyword);
    }

    private void searchByFirstName(String keyword) {
        greetingService.getContactInfosByFirstName(keyword, new AsyncCallback<List<ContactInfo>>() {
            public void onFailure(Throwable caught) {
                if (caught instanceof ContactNoneExistsException)
                    GWT.log("Server search failed: " + caught.getMessage());
                else GWT.log("Unexpected error in firstName search", caught);
                searchByFullName(keyword);
            }

            public void onSuccess(List<ContactInfo> result) {
                if (result != null && !result.isEmpty())
                    handleResult(result, "server:firstName");
                else searchByFullName(keyword);
            }
        });
    }

    private void searchByFullName(String keyword) {
        greetingService.getContactInfosByFullName(keyword, new AsyncCallback<List<ContactInfo>>() {
            public void onFailure(Throwable caught) {
                searchByPhoneNumber(keyword);
            }

            public void onSuccess(List<ContactInfo> result) {
                if (result != null && !result.isEmpty())
                    handleResult(result, "server:fullName");
                else searchByPhoneNumber(keyword);
            }
        });
    }

    private void searchByPhoneNumber(String keyword) {
        greetingService.startsWithPhoneNumber(keyword, new AsyncCallback<List<ContactInfo>>() {
            public void onFailure(Throwable caught) {
                showSpinner(false);
                Window.alert("Không tìm thấy liên hệ.");
            }

            public void onSuccess(List<ContactInfo> result) {
                if (result != null && !result.isEmpty())
                    handleResult(result, "server:phoneNumber");
                else Window.alert("Không tìm thấy liên hệ.");
                showSpinner(false);
            }
        });
    }

    private void handleResult(List<ContactInfo> contacts, String source) {
        GWT.log("Found " + contacts.size() + " contacts from " + source);
        dataProvider.getList().clear();
        dataProvider.getList().addAll(contacts);
        dataProvider.refresh();
        ContactInfoCache.setCurrentContacts(contacts);
        showSpinner(false);
    }
}

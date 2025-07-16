
package com.hello.client.activities.addupdate2;

import java.util.Arrays;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import com.hello.client.GreetingServiceAsync;
import com.hello.client.activities.ClientFactory;
import com.hello.client.activities.ClientFactoryImpl;
import com.hello.client.activities.basic.BasicActivity;
import com.hello.client.activities.homepage.HomepagePlace;
import com.hello.shared.model.ContactInfo;
import com.hello.shared.cache.ContactInfoCache;
import com.hello.shared.verifier.ContactInfoVerifier;
import com.hello.shared.enums.ActionType;
import com.hello.shared.enums.Address;
import com.hello.shared.enums.Gender;
import com.hello.shared.exception.ContactAlreadyExistsException;
import com.hello.shared.exception.ContactNoneExistsException;

public class AddUpdateContactActivity2 extends BasicActivity {
	private AddUpdateContactView2 view = clientFactory.getAddUpdateContactView2();
	protected GreetingServiceAsync greetingService = clientFactory.getGreetingService();
	protected ListDataProvider<ContactInfo> dataProvider = clientFactory.getDataProvider();
	private ActionType actionType;
	protected ContactInfo selectedContact;
	
	protected MultiSelectionModel<ContactInfo> multiSelectionModel;
	private boolean isCallbackHandled = false;

	public AddUpdateContactActivity2(
			ClientFactory clientFactory, 
			Place place) {
		super(clientFactory, place);
	
		String phone = ((AddUpdateContactPlace2) place).getPhoneNumber();
		this.selectedContact = (phone != null) ? ContactInfoCache.getByPhoneNumber(phone) : null;
		
		ActionType actionType = ((AddUpdateContactPlace2) place).getActionType();
		this.actionType = actionType;

		if (this.view == null) {
			GWT.log("Error: AddUpdateContactView2 is null");
			throw new IllegalStateException("AddUpdateContactView2 is null");
		}
	}

	@Override
	public void start(AcceptsOneWidget panel,  com.google.gwt.event.shared.EventBus eventBus) {
		panel.setWidget(view.asWidget());
		super.start(panel, eventBus, view);
        GWT.log("AddUpdateContactActivity2 - bind2");
//        bind(); // Bind mỗi khi start, HandlerManager sẽ quản lý handler
        // Chỉ bind nếu chưa bind
        if (handlerRegistrations.isEmpty()) {
            GWT.log("AddUpdateContactActivity2 - bind2");
            bind();
            GWT.log("AddUpdateContactActivity2 - bind2 - done");
        }
        loadData();
	}

	@Override
    public void onStop() {
        GWT.log("AddUpdateContactActivity2 - onStop: Removing handlers");
        clearHandlers();
        isCallbackHandled = false;
        super.onStop();
    }
	
	@Override
	public void loadData() {
		view.getAddressValueListBox().setAcceptableValues(Arrays.asList(Address.values()));
		view.getGenderValueListBox().setAcceptableValues(Arrays.asList(Gender.values()));

		if (selectedContact != null) {
			view.getFirstNameBox().setText(selectedContact.getFirstName());
			view.getLastNameBox().setText(selectedContact.getLastName());
			view.getGenderValueListBox().setValue(selectedContact.getGender());
			view.getPhoneNumberBox().setText(selectedContact.getPhoneNumber());
			view.getAddressValueListBox().setValue(selectedContact.getAddress());
			view.getActionButton().setText("Update");
		} else {
			view.getActionButton().setText("Add");
		}
	}


	public void clearFields() {
		GWT.log("Add update contact activity time, stack: " + Arrays.toString(new Throwable().getStackTrace()));
		view.getFirstNameBox().setText("");
		view.getLastNameBox().setText("");
		view.getPhoneNumberBox().setText("");
		view.getGenderValueListBox().setValue(null);
		view.getAddressValueListBox().setValue(null);
	}

	@Override
    protected void bind() {
        if (view.getActionButton() == null || view.getCloseButton() == null) {
            GWT.log("Error: ActionButton or CloseButton is null");
            return;
        }
        super.bind();
        addHandlerRegistration(view.getActionButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleSubmit();
            }
        }));
        addHandlerRegistration(view.getCloseButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
//            	handleCallback();
            	clearFields();
                clientFactory.getPlaceController().goTo(new HomepagePlace());
            }
        }));
    }

	private void handleSubmit() {
		String fn = view.getFirstNameBox().getText().trim();
		String ln = view.getLastNameBox().getText().trim();
		Gender gender = view.getGenderValueListBox().getValue();
		String phone = view.getPhoneNumberBox().getText().trim();
		Address address = view.getAddressValueListBox().getValue();

		if (!ContactInfoVerifier.contactInfoVerifier(fn, ln, gender + "", phone, address + "")) {
			Window.alert("Vui lòng nhập đầy đủ và đúng định dạng!");
			return;
		}

		ContactInfo contactInfo = new ContactInfo(fn, ln, gender, phone, address);

		if (actionType == ActionType.ADD) {
			greetingService.addContactInfo(contactInfo, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof ContactAlreadyExistsException) {
						Window.alert("Liên hệ đã tồn tại!");
					} else {
						Window.alert("Lỗi khi thêm liên hệ.");
					}
				}

				@Override
				public void onSuccess(Void result) {
					dataProvider.getList().add(contactInfo);
					ContactInfoCache.addContact(contactInfo);
					dataProvider.refresh();
					Window.alert("Đã thêm liên hệ thành công.");
					handleCallback();
					clientFactory.getPlaceController().goTo(new HomepagePlace());
					
				}
			});
		} else if (actionType == ActionType.UPDATE && selectedContact != null) {
			greetingService.updateContactInfo(selectedContact, contactInfo, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Update failed: " + caught.getClass().getName() + " - " + caught.getMessage());
	                if (caught.getCause() != null) {
	                    GWT.log("Cause: " + caught.getCause().getClass().getName() + " - " + caught.getCause().getMessage());
	                }
	                if (caught instanceof ContactNoneExistsException || 
	                    (caught.getCause() != null && caught.getCause() instanceof ContactNoneExistsException)) {
	                    Window.alert("Lỗi: Liên hệ không tồn tại!");
	                } else if (caught instanceof ContactAlreadyExistsException || 
	                           (caught.getCause() != null && caught.getCause() instanceof ContactAlreadyExistsException)) {
	                    Window.alert("Lỗi: Số điện thoại đã được sử dụng!");
	                } else {
	                    Window.alert("Lỗi khi cập nhật liên hệ: " + caught.getMessage());
	                }
				}

				@Override
				public void onSuccess(Void result) {
					dataProvider.getList().remove(selectedContact);
					dataProvider.getList().add(contactInfo);
					dataProvider.refresh();
					ContactInfoCache.removeContact(selectedContact);
					ContactInfoCache.addContact(contactInfo);
					Window.alert("Cập nhật thành công.");
					handleCallback();
					clientFactory.getPlaceController().goTo(new HomepagePlace());
				}
			});
		}
	}

    private void handleCallback() {
    	if (!isCallbackHandled) {
            GWT.log("AddUpdateContactActivity2 - handleCallback");
            clearFields();
            isCallbackHandled = true;
        }
    }
	
	public ActionType getActionType() {
		return actionType;
	}

	public ContactInfo getSelectedContact() {
		return selectedContact;
	}
}

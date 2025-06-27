
package com.hello.client.activities.addupdate2;

import java.util.Arrays;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

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
	private AddUpdateContactView2 view;
	protected GreetingServiceAsync greetingService;

	protected ListDataProvider<ContactInfo> dataProvider;
	protected MultiSelectionModel<ContactInfo> multiSelectionModel;
	private ActionType actionType;
	protected ContactInfo selectedContact;
//	protected Set<ContactInfo> selectedContacts;

	private Runnable onCompleteCallback;

	public AddUpdateContactActivity2(ClientFactory clientFactory, Place place, GreetingServiceAsync greetingService,
			ListDataProvider<ContactInfo> dataProvider,

			ActionType actionType, Runnable onCompleteCallback) {
		super(clientFactory, place);
		this.view = clientFactory.getAddUpdateContactView2();
		this.greetingService = greetingService;
		this.dataProvider = dataProvider;
		this.actionType = actionType;

		String phone = ((AddUpdateContactPlace2) place).getPhoneNumber();
		this.selectedContact = (phone != null) ? ContactInfoCache.getByPhoneNumber(phone) : null;
//		this.onCompleteCallback = onCompleteCallback;
	}

	@Override
	public void start(AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus) {
		panel.setWidget(view.asWidget());
		super.start(panel, eventBus, view);
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
		view.getFirstNameBox().setText("");
		view.getLastNameBox().setText("");
		view.getPhoneNumberBox().setText("");
		view.getGenderValueListBox().setValue(null);
		view.getAddressValueListBox().setValue(null);
	}

	@Override
	protected void bind() {
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
				clientFactory.getPlaceController().goTo(new HomepagePlace());
				handleCallback();
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
					clientFactory.getPlaceController().goTo(new HomepagePlace());
					handleCallback();
				}
			});
		} else if (actionType == ActionType.UPDATE && selectedContact != null) {
			greetingService.updateContactInfo(selectedContact, contactInfo, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof ContactAlreadyExistsException
							|| caught instanceof ContactNoneExistsException) {
						Window.alert("Lỗi: " + caught.getMessage());
					} else {
						Window.alert("Lỗi khi cập nhật liên hệ.");
					}
				}

				@Override
				public void onSuccess(Void result) {
					dataProvider.getList().remove(selectedContact);
					dataProvider.getList().add(contactInfo);
					ContactInfoCache.removeContact(selectedContact);
					ContactInfoCache.addContact(contactInfo);
					dataProvider.refresh();
					Window.alert("Cập nhật thành công.");
					clientFactory.getPlaceController().goTo(new HomepagePlace());
					handleCallback();
				}
			});
		}
	}

	private void handleCallback() {

		clearFields();
		if (onCompleteCallback != null) {
			onCompleteCallback.run(); // gọi loadData()
		}
	}
}

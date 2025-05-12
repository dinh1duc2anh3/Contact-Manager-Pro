package koolsoft.client;

import koolsoft.client.service.GreetingService;
import koolsoft.client.service.GreetingServiceAsync;
import koolsoft.client.view.HomePage;
import koolsoft.server.GreetingServiceImpl;
import java.util.Set;

import koolsoft.shared.FieldVerifier;
import koolsoft.shared.exception.ContactAlreadyExistsException;
import koolsoft.shared.ContactInfoDTO;
import koolsoft.shared.ContactInfoVerifier;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;

import java_cup.internal_error;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Demo2 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

//	private final ContactServiceAsync contactService = GWT.create(ContactService.class);

	private ListDataProvider<ContactInfoDTO> dataProvider = new ListDataProvider<>();
	
	private Set<ContactInfoDTO> selectedContacts = null;
	
	private ContactInfoDTO selectedContact = null;


	/**
	 * This is the entry point method.
	 */
	/**
	 *
	 */
	public void onModuleLoad() {

		HomePage homePage = new HomePage();
        RootPanel.get("mainContainer").add(homePage);

	}
//---------------------------------------------------------------------------------------------------------------------
}

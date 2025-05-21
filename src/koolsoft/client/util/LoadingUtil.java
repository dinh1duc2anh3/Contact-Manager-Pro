package koolsoft.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;

public class LoadingUtil {
	public static void showSpinner() {
	    Element spinner = Document.get().getElementById("searchSpinner");
	    if (spinner != null) {
	        spinner.getStyle().setDisplay(com.google.gwt.dom.client.Style.Display.INLINE_BLOCK);
	    }
	}

	public static void hideSpinner() {
	    Element spinner = Document.get().getElementById("searchSpinner");
	    if (spinner != null) {
	        spinner.getStyle().setDisplay(com.google.gwt.dom.client.Style.Display.NONE);
	    }
}
}
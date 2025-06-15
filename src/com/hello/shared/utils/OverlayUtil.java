package com.hello.shared.utils;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class OverlayUtil {
	public static SimplePanel createOverlay() {
		SimplePanel overlay = new SimplePanel();
		overlay.getElement().setId("dialogOverlay");
		overlay.setStyleName("modal-overlay");

		return overlay;
	}

	/**
	 * Safely removes overlay from the page
	 */
	public static void removeOverlay(SimplePanel overlay) {
		if (overlay != null && RootPanel.get().getWidgetIndex(overlay) != -1) {
			RootPanel.get().remove(overlay);
		}
	}

	public static void displayOverlay(SimplePanel overlay) {
		// Only add if not already present
		if (RootPanel.get().getWidgetIndex(overlay) == -1) {
			RootPanel.get().add(overlay);
		}
	}

}

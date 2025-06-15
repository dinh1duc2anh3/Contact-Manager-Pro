package com.hello.client.activities.basic.widget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LayoutWrapper {
	// singleton 
	private static LayoutWrapper onlyLayoutWrapper;
	private static boolean isAttached = false;
	
	private final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
    private final SimplePanel mainContent = new SimplePanel();

	public LayoutWrapper() {

		// prevent duplication
		HeaderPanel header = new HeaderPanel();
		FooterPanel footer = new FooterPanel();

		RootLayoutPanel.get().clear();
		dockLayoutPanel.addNorth(header, 100);
		dockLayoutPanel.addSouth(footer, 80);
		dockLayoutPanel.add(mainContent);

		if (!isAttached) {
			RootLayoutPanel.get().add(dockLayoutPanel);
			isAttached = true;
		}
	}

    public void setMainContent(Widget content) {
        mainContent.setWidget(content);
    }
    
    public static LayoutWrapper getOnlyLayoutWrapper() {
        if (onlyLayoutWrapper == null) {
        	onlyLayoutWrapper = new LayoutWrapper();
        }
        return onlyLayoutWrapper;
    }

}

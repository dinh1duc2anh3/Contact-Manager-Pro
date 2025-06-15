package com.hello.client.activities.basic;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hello.client.activities.basic.widget.FooterPanel;
import com.hello.client.activities.basic.widget.HeaderPanel;

public class BasicViewImpl implements BasicView {

	private BasicViewImplUiBinder basicUiBinder = GWT
			.create(BasicViewImplUiBinder.class);

	interface BasicViewImplUiBinder extends UiBinder<Widget, Layout> {

	}
	protected final Layout layout;

	public static class Layout {
		private final BasicViewImpl basicView;
		@UiField HTMLPanel mainPanel, panelContentBasic;
		@UiField FlowPanel containerPanel;

		public Layout(BasicViewImpl basicView) {
			this.basicView = basicView;
		}

		public Layout() {
			this.basicView = null;
		}

		public HTMLPanel getMainPanel() {
			return mainPanel;
		}

		public BasicViewImpl getBasicView() {
			return basicView;
		}

		public void refreshLayout() {

		}

		public HTMLPanel getPanelContentBasic() {
			return panelContentBasic;
		}
		
		public FlowPanel getContainerPanel() {
			return containerPanel;
		}
	}
	
	public BasicViewImpl() {
		this.layout = new Layout(this);
		basicUiBinder.createAndBindUi(this.layout);
		
	}

	@Override
	public Widget asWidget() {
		return layout.mainPanel;
	}

	@Override
	public Layout getLayout() {
		return layout;
	}

	@Override
	public void refreshView() {
		
	}
}
package com.hello.client.activities.basic;
import com.google.gwt.user.client.ui.IsWidget;
import com.hello.client.activities.basic.BasicViewImpl.Layout;

public interface BasicView{
	public IsWidget asWidget();
	public void refreshView();
	Layout getLayout();
}

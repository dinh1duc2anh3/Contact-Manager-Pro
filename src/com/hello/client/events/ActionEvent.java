package com.hello.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.hello.shared.model.User;

public class ActionEvent extends GwtEvent<ActionEventHandler>{
	public enum Action{
		EDIT, DELETE, ADD, CLICK, VIEW, INFO, GATHERING_INFO, EXPORT, RESET
	}

	public static Type<ActionEventHandler> TYPE = new Type<ActionEventHandler>();
	
	private Action action;
	private User user;
	
	public ActionEvent(Action action, User user) {
		this.action = action;
		this.user = user;
	}
	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ActionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ActionEventHandler handler) {
		handler.onEvent(this);
	}
	
	public Action getAction() {
		return action;
	}
	
	public User getUser() {
		return user;
	}
	
}
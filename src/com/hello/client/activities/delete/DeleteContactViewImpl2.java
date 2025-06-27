package com.hello.client.activities.delete;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.hello.client.activities.basic.BasicViewImpl;

public class DeleteContactViewImpl2 extends BasicViewImpl implements DeleteContactView2 {

    interface DeleteContactViewImplUiBinder extends UiBinder<Widget, DeleteContactViewImpl2> {}
    private static final DeleteContactViewImplUiBinder uiBinder = GWT.create(DeleteContactViewImplUiBinder.class);

    @UiField Label confirmLabel;
    @UiField Button yesButton;
    @UiField Button noButton;

    public DeleteContactViewImpl2() {
        this.layout.getContainerPanel().add(uiBinder.createAndBindUi(this));
    }

    @Override public Label getConfirmLabel() { return confirmLabel; }
    @Override public Button getYesButton() { return yesButton; }
    @Override public Button getNoButton() { return noButton; }
}

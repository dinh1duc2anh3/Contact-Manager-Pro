package com.hello.client.activities.deleteDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.hello.client.activities.basic.BasicViewImpl;
import com.hello.shared.utils.OverlayUtil;
import com.hello.shared.model.ContactInfo;

public class DeleteContactViewImpl extends BasicViewImpl implements DeleteContactView {

    interface DeleteContactViewImplUiBinder extends UiBinder<Widget, DeleteContactViewImpl> {}
    private static DeleteContactViewImplUiBinder uiBinder = GWT.create(DeleteContactViewImplUiBinder.class);

    public DeleteContactViewImpl() {
        this.layout.getContainerPanel().add(uiBinder.createAndBindUi(this));
    }

    private SimplePanel overlay;

    @UiField DialogBox dialogBox;
    @UiField Label deleteConfirmLabel;
    @UiField Button closeDeleteInfoButton;
    @UiField Button deleteInfoButton;

    @Override
    public void setOverlay(SimplePanel overlay) {
        this.overlay = overlay;
    }

    @Override
    public SimplePanel getOverlay() {
        return overlay;
    }

    @Override
    public DialogBox getDialogBox() {
        return dialogBox;
    }

    @Override
    public Button getCloseButton() {
        return closeDeleteInfoButton;
    }

    @Override
    public Button getDeleteButton() {
        return deleteInfoButton;
    }

    @Override
    public Label getDeleteConfirmLabel() {
        return deleteConfirmLabel;
    }

    @Override
    public void showDialogBox() {
        OverlayUtil.displayOverlay(overlay);

        deleteConfirmLabel.setText("Are you sure you want to delete these contacts?");
        
        deleteInfoButton.setEnabled(true);
        dialogBox.center();
        dialogBox.show();
    }

    @Override
    public void closeDialogBox() {
        dialogBox.hide();
        OverlayUtil.removeOverlay(overlay);

        if (deleteInfoButton != null) {
            deleteInfoButton.setEnabled(false);
        }
    }

    @Override
    public void initDialogStyle() {
        dialogBox.addAttachHandler(event -> {
            if (event.isAttached()) {
                Element dialogElement = dialogBox.getElement();
                dialogElement.getStyle().setWidth(350, Unit.PX);
                dialogElement.getStyle().setProperty("boxSizing", "border-box");
            }
        });
    }
}
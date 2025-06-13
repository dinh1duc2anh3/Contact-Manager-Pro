package com.hello.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;


	// GWT yêu cầu constructor không tham số
public class ContactAlreadyExistsException extends Exception implements IsSerializable {
    public ContactAlreadyExistsException() {}
    public ContactAlreadyExistsException(String message) {
        super(message);
    }
}

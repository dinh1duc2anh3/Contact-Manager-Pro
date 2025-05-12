package koolsoft.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

	// GWT yêu cầu constructor không tham số
public class ContactNoneExistsException extends Exception  implements IsSerializable {
    public ContactNoneExistsException() {}

    public ContactNoneExistsException(String message) {
        super(message);
    }
}

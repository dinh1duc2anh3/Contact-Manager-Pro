package com.hello.client;

public class ClientUtils {
	
	public static native void log(String message) /*-{
		$wnd.console.log(message);
	}-*/;

}

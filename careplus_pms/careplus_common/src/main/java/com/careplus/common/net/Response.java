package com.careplus.common.net;

import java.io.Serializable;

/*
 * Response Sent from the server
 *
 * The server half of the protocol, the mirror of Request. 
 *
 * A request the server has no handler for still comes back
 * as a Response rather than an error, so always check getSuccess() instead of
 * assuming there's something in getData().
 */

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean success;
	private String message;

	/*
	 * Whatever the answer is, cast by the caller based on which RequestType they
	 * sent. Anything put in here has to be Serializable, including every item if
	 * it's a list, otherwise the write blows up at runtime.
	 */
	private Object data;

	public Response() {
		success = null;
		message = null;
		data = null;
	}

	public Response(Boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [success=" + success + ", message=" + message + "\n, data=" + data + "]";
	}
	
	

}

package com.careplus.common.net;

import java.io.Serializable;

/*
 * Response Sent from the server
 */

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean success;
	private String Message;
	private Object data;

	public Response() {
		success = null;
		Message = null;
		data = null;
	}

	public Response(Boolean success, String message, Object data) {
		this.success = success;
		Message = message;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [success=" + success + ", Message=" + Message + "\n, data=" + data + "]";
	}
	
	

}

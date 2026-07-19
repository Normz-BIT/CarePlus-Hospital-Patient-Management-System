package com.careplus.common.net;

import java.io.Serializable;

/*
 * Response Sent from the server
 *
 * The server half of the wire protocol, mirroring Request. Same serialization
 * caveat applies: this travels over ObjectOutputStream, so field changes are
 * breaking changes for any client running an older build.
 *
 * Note that a request the server does not handle still comes back as a Response
 * with all three fields null rather than as an error, because the dispatch switch
 * in ClientHandler falls through to default for the RequestType values that have
 * no service behind them yet. Callers must therefore null check getSuccess()
 * rather than assuming a populated result.
 */

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Boxed rather than primitive so that "never set" is distinguishable from an
	 * explicit false. An unhandled request yields null here, a genuinely failed
	 * one yields FALSE.
	 */
	private Boolean success;
	private String message;

	/*
	 * Untyped payload, cast by the caller according to the RequestType that was
	 * sent. Whatever is placed here must itself be Serializable, including every
	 * element if it is a collection, or the write fails at runtime rather than at
	 * compile time.
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

package com.careplus.common.net;

import java.io.Serializable;
import java.util.Map;

/**
 * Request Sent From the client to the server
 */

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestType type;

	private Map<String, Object> params;

	private String SessionToken;

	protected Request() {

	}

	protected Request(RequestType type, Map<String, Object> params, String sessionToken) {
		this.type = type;
		this.params = params;
		SessionToken = sessionToken;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getSessionToken() {
		return SessionToken;
	}

	public void setSessionToken(String sessionToken) {
		SessionToken = sessionToken;
	}	

}

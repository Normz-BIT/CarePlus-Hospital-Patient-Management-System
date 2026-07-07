package com.careplus.common.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Sent From the client to the server
 */

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestType type;

	private Map<String, Object> params;

	public Request() {
		type = null;
		params = new HashMap<>();
	}

	public Request(RequestType type, Map<String, Object> params) {
		this.type = type;
		this.params = params;
	}

	public Request(RequestType type, String key, Object Value) {
		this.type = type;
		
		if (params == null) {
			params = new HashMap<>();
		}
		putMap(key, Value);
	}

	// delegate for the Map
	public void putMap(String key, Object value) {
		params.put(key, value);
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

}

package com.careplus.common.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Sent From the client to the server
 *
 * This is one half of the wire protocol. Instances are written directly onto an
 * ObjectOutputStream by Client.send and read back by ClientHandler, so this class
 * is a published binary contract: renaming or retyping a field silently breaks
 * every client built against an older jar. Bump serialVersionUID deliberately if
 * the shape ever has to change, rather than letting the compiler generate one.
 */

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestType type;

	/*
	 * Deliberately an untyped bag rather than a per-action DTO. It keeps one
	 * envelope class serving all 26 RequestType values, but the cost is that
	 * parameter keys and value types are a convention only: the compiler cannot
	 * catch a misspelled key or a wrong cast, so mismatches surface at runtime on
	 * the server as a ClassCastException inside the dispatch switch.
	 */
	private Map<String, Object> params;

	public Request() {
		type = null;
		params = new HashMap<>();
	}

	/*
	 * Takes the caller's map by reference rather than copying it. The caller must
	 * not mutate the map after handing it over, since serialization happens later
	 * on whichever thread calls Client.send.
	 */
	public Request(RequestType type, Map<String, Object> params) {
		this.type = type;
		this.params = params;
	}

	/*
	 * Convenience path for the common single-parameter case, such as looking up
	 * records by patient ID.
	 */
	public Request(RequestType type, String key, Object Value) {
		this.type = type;

		/*
		 * params carries no field initializer, so it is always null on entry here and
		 * this branch always fires. The guard is redundant but harmless, and removing
		 * it would leave putMap dereferencing null.
		 */
		if (params == null) {
			params = new HashMap<>();
		}
		putMap(key, Value);
	}

	/*
	 * Callers must go through a constructor first. Invoking this on an instance
	 * built by a path that left params null would throw, which is why every
	 * constructor above guarantees a non-null map.
	 */
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

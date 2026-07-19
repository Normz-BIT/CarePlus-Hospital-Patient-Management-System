package com.careplus.common.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Sent From the client to the server
 *
 * This is one half of our wire protocol. Instances are written straight onto an
 * ObjectOutputStream by Client.send and read back by ClientHandler, so the shape
 * of this class is shared by both sides and has to stay in step between them.
 *
 * We set serialVersionUID by hand rather than letting the compiler generate one,
 * so the value only changes when we decide it should. A generated identifier
 * would change whenever the class was edited, and every client built against an
 * older jar would stop being able to read it.
 */

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestType type;

	/*
	 * We chose a general map of parameters rather than a separate request class per
	 * action. One envelope serves every RequestType, so adding a feature does not
	 * mean adding a new class to both modules, and the protocol stays small enough
	 * to reason about as a group.
	 *
	 * The trade off we accepted is that parameter names and value types are a
	 * convention between client and server rather than something the compiler
	 * checks, so both sides have to agree on the keys each request uses.
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

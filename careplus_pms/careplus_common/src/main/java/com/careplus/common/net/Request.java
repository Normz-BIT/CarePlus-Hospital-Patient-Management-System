package com.careplus.common.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Sent From the client to the server
 *
 * One half of our protocol. These get written straight onto an
 * ObjectOutputStream by Client.send and read back by ClientHandler, so both
 * sides share this class and it has to stay in step between them.
 *
 * We set serialVersionUID ourselves instead of letting the compiler make one up,
 * so it only changes when we say so. A generated one changes every time the
 * class is edited, and then any client built against an older jar can't read it
 * any more.
 */

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestType type;

	/*
	 * We went with one general map of parameters instead of a separate request
	 * class for every action. One envelope covers every RequestType, so adding a
	 * feature doesn't mean adding a class to both modules, and the whole protocol
	 * stays small enough for all of us to keep in our heads.
	 *
	 * The tradeoff we accepted is that the key names and value types are just an
	 * agreement between client and server, nothing the compiler checks. So if you
	 * change a key name on one side you have to change it on the other.
	 */
	private Map<String, Object> params;

	public Request() {
		type = null;
		params = new HashMap<>();
	}

	/*
	 * This keeps the caller's map rather than copying it, so don't change the map
	 * after handing it over. It doesn't get serialized until later on whatever
	 * thread calls Client.send.
	 */
	public Request(RequestType type, Map<String, Object> params) {
		this.type = type;
		this.params = params;
	}

	/*
	 * Shortcut for the common one-parameter case, like looking records up by
	 * patient ID.
	 */
	public Request(RequestType type, String key, Object Value) {
		this.type = type;

		/*
		 * params has no initializer on the field, so it's always null by the time we
		 * get here and this if always runs.
		 */
		if (params == null) {
			params = new HashMap<>();
		}
		putMap(key, Value);
	}

	/*
	 * Always build a Request through one of the constructors above first. Every one
	 * of them makes sure params isn't null, because this method would throw if it
	 * were.
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

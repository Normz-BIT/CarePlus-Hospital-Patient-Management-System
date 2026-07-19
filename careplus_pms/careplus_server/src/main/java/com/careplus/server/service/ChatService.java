package com.careplus.server.service;

import java.util.List;

import com.careplus.common.model.ChatMessages;
import com.careplus.common.net.Response;

/*
 * ChatService
 * Intended home of the live chat feature between patients and staff.
 *
 * NOT YET IMPLEMENTED. Every method below is a placeholder returning null or
 * false, and no CHAT_SEND or CHAT_POLL case exists in ClientHandler's dispatch
 * switch, so chat requests from either client currently return an empty Response.
 * The signatures are committed ahead of the bodies so the client controllers can
 * be written against the final shape.
 *
 * Note this class does not extend BaseService, unlike the two working services.
 * It will need to once it touches the database, so that it picks up the shared
 * session and transaction handling instead of managing Hibernate itself.
 */
public class ChatService {

	public Response send(ChatMessages message) {
		return null;

	}

	/*
	 * Polling rather than server push, matching the request and response shape of
	 * the socket protocol: the server never writes to a client that has not just
	 * asked for something, so unread messages are only discovered when the client
	 * calls this.
	 *
	 * The userId parameter is an int while Person identifiers are Strings elsewhere
	 * in the system, so this signature will need reconciling before it can be wired
	 * up.
	 */
	public List<ChatMessages> poll(int userId){
		return null;
	}

	/*
	 * Enforces the hospital operating hours rule: live chat is only available
	 * between 8:00 a.m. and 7:00 p.m.
	 *
	 * Currently returns false unconditionally, so the rule is declared but not
	 * enforced anywhere. Neither client checks the time of day either, which means
	 * chat is effectively ungated rather than closed. Once implemented, send() and
	 * poll() should both consult this, and the check belongs here on the server
	 * rather than in the client, since a client's clock can be wrong or altered.
	 */
	public boolean isWithinHours() {
		return false;
	}

}

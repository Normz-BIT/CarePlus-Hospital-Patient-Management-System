package com.careplus.server.service;

import java.time.LocalTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.enums.UserRole;
import com.careplus.common.model.ChatMessage;
import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * ChatService
 * Live chat between patients and staff. It's polled, not pushed: our protocol is
 * strictly one reply per request so the server can't write to a client out of
 * the blue. The client has to keep asking with CHAT_POLL instead.
 */
public class ChatService extends BaseService {

	private static final Logger logger = LogManager.getLogger(ChatService.class);

	/*
	 * Hospital opening hours from the brief. Pulled out as constants so they're
	 * easy to widen if we end up demoing in the evening. The check lives here on
	 * the server because a client's clock can be wrong or changed on purpose.
	 */
	private static final LocalTime OPENS = LocalTime.of(8, 0);
	private static final LocalTime CLOSES = LocalTime.of(19, 0);

	public Response send(Request request) {

		ChatMessage message = (ChatMessage) request.getParams().get("chatMessage");
		String recipient = (String) request.getParams().get("recipient");

		/*
		 * Only sending is blocked outside hours. Polling stays open so people can
		 * still read back their conversation after closing time.
		 */
		if (!isWithinHours()) {

			resp = new Response();
			resp.setSuccess(false);
			resp.setMessage("Live chat is only available between 8:00 a.m. and 7:00 p.m.");

			logger.info("Chat message rejected outside operating hours");

			return resp;
		}

		startSession();

		try {

			message.setReceiverId(resolveRecipient(recipient));

			session.persist(message);

			resp.setSuccess(true);
			resp.setMessage("Message sent");

			logger.info("Chat message from {} to {}", message.getSenderId(), message.getReceiverId());

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to send message: " + e.getMessage());

			logger.error("Could not send chat message", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * A conversation, both directions, oldest first so it reads top to bottom like
	 * a chat should. While we're in here we also flip isRead on anything addressed
	 * to this user, since polling is the moment they've actually seen it, and that
	 * flag is what tells the other side their message was read.
	 *
	 * The optional "with" param narrows it to the conversation with one specific
	 * person. Without it you get every message the user has ever sent or received
	 * jumbled into one list, which was fine back when a patient could only really
	 * reach one member of staff, but reads as a mess now they can pick who they're
	 * talking to. Left out, it still returns everything, so a screen that wants the
	 * lot can just not send it.
	 */
	public Response poll(Request request) {

		String userId = (String) request.getParams().get("user");
		String withId = (String) request.getParams().get("with");

		startSession();

		try {
			List<ChatMessage> messages;

			if (withId == null || withId.trim().isEmpty()) {

				messages = session
						.createQuery("FROM ChatMessage WHERE senderId = ?1 OR receiverId = ?1 ORDER BY sentAt",
								ChatMessage.class)
						.setParameter(1, userId).list();

			} else {
				/*
				 * Both directions between exactly these two people: what they sent us and what
				 * we sent them, nothing else.
				 */
				messages = session
						.createQuery("FROM ChatMessage WHERE (senderId = ?1 AND receiverId = ?2) "
								+ "OR (senderId = ?2 AND receiverId = ?1) ORDER BY sentAt", ChatMessage.class)
						.setParameter(1, userId).setParameter(2, withId.trim().toUpperCase()).list();
			}

			for (ChatMessage message : messages) {

				if (userId.equals(message.getReceiverId()) && !Boolean.TRUE.equals(message.getIsRead())) {
					// These are attached to the session, so this saves when endSession commits.
					message.setIsRead(true);
				}
			}

			resp.setSuccess(true);
			resp.setMessage("Messages found");
			resp.setData(messages);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get messages");

			logger.error("Could not load chat messages for {}", userId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	public boolean isWithinHours() {

		LocalTime now = LocalTime.now();

		return !now.isBefore(OPENS) && now.isBefore(CLOSES);
	}

	/*
	 * The two chat screens send different things here. Staff pick an actual patient
	 * from a combo, but the patient side just picks a role name ("Receptionist",
	 * "Doctor", "Nurse"). So we try it as an ID first, and if that finds nothing we
	 * treat it as a role and give the message to the first staff member with that
	 * role. Routing it to whoever is actually on duty would need a rota, and we
	 * don't have one of those.
	 */
	private String resolveRecipient(String recipient) {

		if (recipient == null || recipient.trim().isEmpty()) {
			throw new IllegalArgumentException("No recipient given");
		}

		String id = recipient.trim().toUpperCase();

		if (session.find(Person.class, id) != null) {
			return id;
		}

		for (UserRole role : UserRole.values()) {

			if (role.name().equalsIgnoreCase(recipient.trim())) {

				List<String> ids = session
						.createQuery("SELECT p.personId FROM Person p WHERE p.role = ?1 ORDER BY p.personId",
								String.class)
						.setParameter(1, role).setMaxResults(1).list();

				if (!ids.isEmpty()) {
					return ids.get(0);
				}
			}
		}

		throw new IllegalArgumentException("No person or role matches \"" + recipient + "\"");
	}
}

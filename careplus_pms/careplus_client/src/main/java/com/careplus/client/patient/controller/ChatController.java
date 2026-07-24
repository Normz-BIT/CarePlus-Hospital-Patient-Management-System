package com.careplus.client.patient.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.ChatView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.ChatMessage;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Chat Controller
 * Patient side of the live chat with receptionists, doctors and nurses
 *
 * Chat works by polling rather than the server pushing messages out. The client
 * asks for the conversation when the screen opens and whenever the patient
 * refreshes it. We chose polling because our protocol is strictly one response
 * per request, so the server has no way to write to a client unprompted.
 *
 * The operating hours rule (8:00 a.m. to 7:00 p.m.) lives in ChatService on the
 * server, so a message sent after hours comes back rejected with a message
 * saying so. We don't duplicate the check here because a client clock can be
 * wrong or changed.
 */
public class ChatController {
	private final ChatView view;
	private static final Logger logger = LogManager.getLogger(ChatController.class);

	public ChatController(ChatView view) {
		this.view = view;
		loadStaff();
		refresh();
	}

	/*
	 * Fill the recipient combo with the actual staff from the server.
	 *
	 * This used to be three hardcoded role names, and the server turned "Doctor"
	 * into whichever doctor happened to sort first, so a patient could never reach
	 * the second one. Now they pick the person.
	 *
	 * Quiet on failure, same as the booking screen's combos: an empty combo is
	 * better than refusing to open the screen.
	 */
	private void loadStaff() {
		Response res = Client.send(new Request(RequestType.GET_STAFF, "all", true));

		view.getCboRecipient().removeAllItems();

		if (res != null && Boolean.TRUE.equals(res.getSuccess()) && res.getData() instanceof List<?> staff) {

			for (Object member : staff) {
				view.getCboRecipient().addItem(String.valueOf(member));
			}
		} else {
			logger.warn("The staff list could not be loaded for the chat combo");
		}
	}

	/*
	 * Pulls "STF0001" off the front of "STF0001 - Karen Reid (DOCTOR)". Same trick
	 * the staff chat and vitals screens use on their patient combos.
	 */
	private String selectedRecipientId() {
		Object selected = view.getCboRecipient().getSelectedItem();

		if (selected == null) {
			return "";
		}

		String display = String.valueOf(selected);
		int dash = display.indexOf(" - ");

		return (dash < 0 ? display : display.substring(0, dash)).trim();
	}

	public void sendMessage() {
		String msg = view.getTxtMessage().getText().trim();
		String recipient = selectedRecipientId();

		if (msg.isEmpty()) {
			view.showMessage("Message is required.");
			logger.warn("Chat message rejected because the message was empty");

			return;
		}

		if (recipient.isEmpty()) {
			view.showMessage("Select who you want to message.");
			logger.warn("Chat message rejected because no recipient was selected");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.CHAT_SEND);

		ChatMessage chatMessage = new ChatMessage();

		try {

			chatMessage.setSenderId(
					String.valueOf(MainDashboard.getCurrentUser().getPersonId()));

			chatMessage.setContent(msg);

			chatMessage.setSentAt(LocalDateTime.now());

			/*
			 * A newly sent message is unread by definition. The recipient's client is what
			 * would flip this, so it stays false until the other side acknowledges.
			 */
			chatMessage.setIsRead(false);

			logger.info("Chat message created: {}", chatMessage.toString());

			req.putMap("chatMessage", chatMessage);
			req.putMap("recipient", recipient);

			Response res = Client.send(req);

			if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
				view.showMessage(
						res == null
								? "No response from server."
								: res.getMessage());

				logger.error("Chat message could not be sent");
				return;
			}

			logger.info("Chat message sent successfully");

			/*
			 * Cleared only after the server confirms the send, so a failed message stays in
			 * the box and the user does not lose what they typed.
			 */
			view.clearMessageField();

		} catch (Exception e) {

			logger.error("An error occurred while sending chat message", e);
			view.showMessage("Unable to send message: " + e.getMessage());
		}

		refresh();

	}

	/*
	 * Chat is pull based: the server never pushes to an idle client, so new messages
	 * only appear when this runs. It fires on open, after a send, and on the manual
	 * refresh button, with no timer, so a patient sees a reply only by clicking.
	 * A Swing Timer polling this would make the conversation feel live, but each tick
	 * would block the Event Dispatch Thread on a socket round trip.
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		/*
		 * "with" narrows this to the conversation with whoever is picked in the combo,
		 * so switching recipient switches conversation instead of showing every message
		 * this patient has ever exchanged with anybody all mixed together.
		 */
		Request req = new Request(
				RequestType.CHAT_POLL,
				"user",
				MainDashboard.getCurrentUser().getPersonId());

		req.putMap("with", selectedRecipientId());

		Response res = Client.send(req);

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Chat messages could not be retrieved");
			return;
		}

		view.clearConversation();

		for (ChatMessage msg : (List<ChatMessage>) res.getData()) {

			String viewMessage =
					"Message ID: " + msg.getMessageId()
					+ "\nSender: " + msg.getSenderId()
					+ "\nMessage: " + msg.getContent()
					+ "\nTime: " + DateDisplay.withTime(msg.getSentAt())
					+ "\nRead: " + msg.getIsRead()
					+ "\n";

			view.appendMessage(viewMessage);
		}

		logger.info("Chat messages refreshed successfully");

	}
}

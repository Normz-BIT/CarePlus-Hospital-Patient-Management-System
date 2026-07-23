package com.careplus.client.employee.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.StaffChatView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.ChatMessage;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Staff Chat Controller
 * Allows employees to exchange messages with patients
 *
 * The employee counterpart to the patient side ChatController, available to all
 * three staff roles because any of them may need to answer a patient. It follows
 * the same poll based design described there.
 *
 * TODO: choose the patient from a list rather than a typed ID, matching how the
 * patient side selects its recipient.
 *
 * TODO: apply the operating hours rule here too, once ChatService enforces it.
 */
public class StaffChatController {
	private final StaffChatView view;
	private static final Logger logger = LogManager.getLogger(StaffChatController.class);

	public StaffChatController(StaffChatView view) {
		this.view = view;
		refresh();
	}

	/*
	 * Send Message to Patient
	 */
	public void sendMessage() {
		String message = view.getTxtMessage().getText().trim();
		String patient = view.getTxtPatient().getText().trim();

		if (message.isEmpty()) {
			view.showMessage("Message is required.");
			logger.warn("Staff chat message rejected because the message was empty");

			return;
		}

		if (patient.isEmpty()) {
			view.showMessage("Enter the patient ID to chat with.");
			logger.warn("Staff chat message rejected because the patient ID was empty");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.CHAT_SEND);

		ChatMessage chatMessage = new ChatMessage();

		try {

			chatMessage.setSenderId(
					String.valueOf(MainDashboard.getCurrentUser().getPersonId()));

			chatMessage.setContent(message);

			chatMessage.setSentAt(LocalDateTime.now());

			chatMessage.setIsRead(false);

			logger.info(
					"Staff chat message created for patient ID: {}",
					patient);

			req.putMap("chatMessage", chatMessage);
			req.putMap("recipient", patient);

			Response res = Client.send(req);

			if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
				view.showMessage(
						res == null
								? "No response from server."
								: res.getMessage());

				logger.error("Staff chat message could not be sent");
				return;
			}

			logger.info("Staff chat message sent successfully");

			view.clearMessageField();

		} catch (Exception e) {

			logger.error("An error occurred while sending staff chat message", e);
			view.showMessage("Unable to send message: " + e.getMessage());
		}

		refresh();

	}

	/*
	 * View Patient Conversation
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		String patient = view.getTxtPatient().getText().trim();

		Response res = Client.send(
				new Request(
						RequestType.CHAT_POLL,
						"user",
						patient.isEmpty()
								? MainDashboard.getCurrentUser().getPersonId()
								: patient));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Staff chat messages could not be retrieved");
			return;
		}

		view.clearConversation();

		for (ChatMessage msg : (List<ChatMessage>) res.getData()) {

			String viewMessage =
					"Message ID: " + msg.getMessageId()
					+ "\nSender: " + msg.getSenderId()
					+ "\nMessage: " + msg.getContent()
					+ "\nTime: " + msg.getSentAt()
					+ "\nRead: " + msg.getIsRead()
					+ "\n";

			view.appendMessage(viewMessage);
		}

		logger.info("Staff chat messages refreshed successfully");

	}
}
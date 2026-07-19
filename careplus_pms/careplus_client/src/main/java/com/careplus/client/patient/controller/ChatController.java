package com.careplus.client.patient.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.ChatView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.ChatMessages;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class ChatController {
	private final ChatView view;
	private static final Logger logger = LogManager.getLogger(ChatController.class);

	public ChatController(ChatView view) {
		this.view = view;
		refresh();
	}

	public void sendMessage() {
		String msg = view.getTxtMessage().getText().trim();

		if (msg.isEmpty()) {
			view.showMessage("Message is required.");
			logger.warn("Chat message rejected because the message was empty");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.CHAT_SEND);

		ChatMessages chatMessage = new ChatMessages();

		try {

			chatMessage.setSenderId(
					String.valueOf(MainDashboard.getCurrentUser().getPersonId()));

			chatMessage.setContent(msg);

			chatMessage.setTimeStamp(LocalDateTime.now());

			chatMessage.setIsRead(false);

			logger.info("Chat message created: {}", chatMessage.toString());

			req.putMap("chatMessage", chatMessage);
			req.putMap(
					"recipient",
					String.valueOf(view.getCboRecipient().getSelectedItem()));

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

			view.clearMessageField();

		} catch (Exception e) {

			logger.error("An error occurred while sending chat message", e);
			view.showMessage("Unable to send message: " + e.getMessage());
		}

		refresh();

	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.CHAT_POLL,
						"user",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Chat messages could not be retrieved");
			return;
		}

		view.clearConversation();

		for (ChatMessages msg : (List<ChatMessages>) res.getData()) {

			String viewMessage =
					"Message ID: " + msg.getMessageId()
					+ "\nSender: " + msg.getSenderId()
					+ "\nMessage: " + msg.getContent()
					+ "\nTime: " + msg.getTimeStamp()
					+ "\nRead: " + msg.getIsRead()
					+ "\n";

			view.appendMessage(viewMessage);
		}

		logger.info("Chat messages refreshed successfully");

	}
}

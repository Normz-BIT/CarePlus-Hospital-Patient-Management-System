package com.careplus.client.employee.controller;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> stash
import java.util.List;

import com.careplus.client.employee.view.StaffChat;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

<<<<<<< HEAD
=======
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
>>>>>>> stash
public class StaffChatController {
	private final StaffChat view;

	public StaffChatController(StaffChat view) {
		this.view = view;
<<<<<<< HEAD
		view.getBtnSend().addActionListener(e -> sendMessage());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearConversation());
=======
>>>>>>> stash
		refresh();
	}

<<<<<<< HEAD
	private void sendMessage() {
=======
	/*
	 * Send Message to Patient
	 */
	public void sendMessage() {
>>>>>>> stash
		String message = view.getTxtMessage().getText().trim();
		String patient = view.getTxtPatient().getText().trim();
		if (message.isEmpty())
			return;
		if (patient.isEmpty()) {
			view.showMessage("Enter the patient ID to chat with.");
			return;
		}

		Request req = new Request();
		req.setType(RequestType.CHAT_SEND);
<<<<<<< HEAD
		req.putMap("sender", "Staff");
		req.putMap("recipient", patient);
		req.putMap("message", message);
		Response res = new Client().send(req);
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
			view.showMessage(res == null ? "No response from server." : res.getMessage());
=======

		ChatMessages chatMessage = new ChatMessages();

		try {

			chatMessage.setSenderId(
					String.valueOf(MainDashboard.getCurrentUser().getPersonId()));

			chatMessage.setContent(message);

			chatMessage.setTimeStamp(LocalDateTime.now());

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
>>>>>>> stash
		}
<<<<<<< HEAD
		view.clearMessageField();
		refresh();
=======

		refresh();

>>>>>>> stash
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		String patient = view.getTxtPatient().getText().trim();
<<<<<<< HEAD
		Response res = new Client()
				.send(new Request(RequestType.CHAT_POLL, "user", patient.isEmpty() ? "current" : patient));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
=======

		Response res = Client.send(
				new Request(
						RequestType.CHAT_POLL,
						"user",
						patient.isEmpty()
								? MainDashboard.getCurrentUser().getPersonId()
								: patient));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Staff chat messages could not be retrieved");
>>>>>>> stash
			return;
		view.clearConversation();
		if (res.getData() instanceof List<?>) {
			for (Object msg : (List<Object>) res.getData()) {
				view.appendMessage(String.valueOf(msg));
			}
		}
	}
}

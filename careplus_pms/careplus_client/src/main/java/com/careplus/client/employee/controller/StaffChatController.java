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
import com.careplus.common.util.DateDisplay;

/*
 * Staff Chat Controller
 * Allows employees to exchange messages with patients
 *
 * The employee counterpart to the patient side ChatController, available to all
 * three staff roles because any of them may need to answer a patient. It follows
 * the same poll based design described there.
 *
 * The patient comes from a combo filled by GET_PATIENTS rather than a typed ID,
 * so a typo can't send a message to nobody or poll an empty conversation.
 */
public class StaffChatController {
	private final StaffChatView view;
	private static final Logger logger = LogManager.getLogger(StaffChatController.class);

	public StaffChatController(StaffChatView view) {
		this.view = view;
		loadPatients();
		refresh();
	}

	/*
	 * Fill the patient combo with "PAT0001 - Name" strings from the server. If the
	 * lookup fails the combo just stays empty, same silent-failure style as the
	 * booking screen's combos.
	 */
	private void loadPatients() {
		Response res = Client.send(new Request(RequestType.GET_PATIENTS, "all", true));

		view.getCboPatient().removeAllItems();

		if (res != null && Boolean.TRUE.equals(res.getSuccess()) && res.getData() instanceof List<?> patients) {

			for (Object patient : patients) {
				view.getCboPatient().addItem(String.valueOf(patient));
			}
		} else {
			logger.warn("The patient list could not be loaded for the staff chat combo");
		}
	}

	/*
	 * Pulls "PAT0001" back off the front of the combo's "PAT0001 - Name" display
	 * string. Empty when nothing is selected.
	 */
	private String selectedPatientId() {
		Object selected = view.getCboPatient().getSelectedItem();

		if (selected == null) {
			return "";
		}

		String display = String.valueOf(selected);
		int dash = display.indexOf(" - ");

		return (dash < 0 ? display : display.substring(0, dash)).trim();
	}

	/*
	 * Send Message to Patient
	 */
	public void sendMessage() {
		String message = view.getTxtMessage().getText().trim();
		String patient = selectedPatientId();

		if (message.isEmpty()) {
			view.showMessage("Message is required.");
			logger.warn("Staff chat message rejected because the message was empty");

			return;
		}

		if (patient.isEmpty()) {
			view.showMessage("Select the patient to chat with.");
			logger.warn("Staff chat message rejected because no patient was selected");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.CHAT_SEND);

		ChatMessage chatMessage = new ChatMessage();

		try {

			chatMessage.setSenderId(String.valueOf(MainDashboard.getCurrentUser().getPersonId()));

			chatMessage.setContent(message);

			chatMessage.setSentAt(LocalDateTime.now());

			chatMessage.setIsRead(false);

			logger.info("Staff chat message created for patient ID: {}", patient);

			req.putMap("chatMessage", chatMessage);
			req.putMap("recipient", patient);

			Response res = Client.send(req);

			if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
				view.showMessage(res == null ? "No response from server." : res.getMessage());

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
	 *
	 * Polls the selected patient's conversation, or our own when the combo is
	 * still empty (say the patient list failed to load).
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		String patient = selectedPatientId();

		/*
		 * We poll as ourselves and narrow it with "with" to the selected patient, so
		 * this shows our conversation with them rather than every message that patient
		 * has had with anyone. That's what it used to do, because it polled as the
		 * patient instead of as us.
		 */
		Request req = new Request(
				RequestType.CHAT_POLL,
				"user",
				MainDashboard.getCurrentUser().getPersonId());

		req.putMap("with", patient);

		Response res = Client.send(req);

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Staff chat messages could not be retrieved");
			return;
		}

		view.clearConversation();

		for (ChatMessage msg : (List<ChatMessage>) res.getData()) {

			String viewMessage = "Message ID: " + msg.getMessageId() + "\nSender: " + msg.getSenderId() + "\nMessage: "
					+ msg.getContent() + "\nTime: " + DateDisplay.withTime(msg.getSentAt())
					+ "\nRead: " + msg.getIsRead() + "\n";

			view.appendMessage(viewMessage);
		}

		logger.info("Staff chat messages refreshed successfully");

	}
}
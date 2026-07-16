package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.StaffChat;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class StaffChatController {
	private final StaffChat view;

	public StaffChatController(StaffChat view) {
		this.view = view;
		view.getBtnSend().addActionListener(e -> sendMessage());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearConversation());
		refresh();
	}

	private void sendMessage() {
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
		req.putMap("sender", "Staff");
		req.putMap("recipient", patient);
		req.putMap("message", message);
		Response res = Client.send(req);
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
			view.showMessage(res == null ? "No response from server." : res.getMessage());
		}
		view.clearMessageField();
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		String patient = view.getTxtPatient().getText().trim();
		Response res = Client.send(new Request(RequestType.CHAT_POLL, "user", patient.isEmpty() ? "current" : patient));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		view.clearConversation();
		if (res.getData() instanceof List<?>) {
			for (Object msg : (List<Object>) res.getData()) {
				view.appendMessage(String.valueOf(msg));
			}
		}
	}
}

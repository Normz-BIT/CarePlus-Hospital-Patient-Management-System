package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.ChatView;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class ChatController {
	private final ChatView view;

	public ChatController(ChatView view) {
		this.view = view;
		view.getBtnSend().addActionListener(e -> sendMessage());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearConversation());
		refresh();
	}

	private void sendMessage() {
		String msg = view.getTxtMessage().getText().trim();
		if (msg.isEmpty())
			return;
		Request req = new Request();
		req.setType(RequestType.CHAT_SEND);
		req.putMap("sender", "Patient");
		req.putMap("recipient", String.valueOf(view.getCboRecipient().getSelectedItem()));
		req.putMap("message", msg);
		Response res = Client.send(req);
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			view.showMessage(res == null ? "No response from server." : res.getMessage());
		view.clearMessageField();
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.CHAT_POLL, "user", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		view.clearConversation();
		if (res.getData() instanceof List<?>)
			for (Object msg : (List<Object>) res.getData())
				view.appendMessage(String.valueOf(msg));
	}
}

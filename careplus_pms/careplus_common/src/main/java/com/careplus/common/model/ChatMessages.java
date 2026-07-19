package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Chat Messages for all users
 *
 * DTO ONLY: no JPA annotations, matching ChatService still being an unimplemented
 * stub.
 *
 * Only the sender is recorded, with no recipient field, so as modelled a message
 * cannot be routed to a particular member of staff or back to a particular
 * patient. A recipient is needed before ChatService.poll can answer "what is
 * waiting for me".
 * */

public class ChatMessages implements Serializable {
	private static final long serialVersionUID = 1L;

	private int messageId;
	/*
	 * A String matching Person.personId, so patients and staff share one identifier
	 * space and either can be a sender. Note ChatService.poll takes an int, an
	 * inconsistency that has to be resolved before the two can be wired together.
	 */
	private String senderId;
	private String content;
	/*
	 * Orders the conversation, and is also what any operating hours rule would be
	 * evaluated against. That check belongs on the server rather than here, since a
	 * client clock can be wrong or deliberately altered.
	 */
	private LocalDateTime timeStamp;
	/*
	 * Boxed rather than primitive, so null means "never set" as distinct from an
	 * explicit unread. Callers should treat null as unread rather than assume it is
	 * populated.
	 */
	private Boolean isRead;

	public ChatMessages() {

	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}


	@Override
	public String toString() {
		return "ChatMessages [messageId=" + messageId + ", senderId=" + senderId + ", content=" + content
				+ ", timeStamp=" + timeStamp + ", isRead=" + isRead + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ChatMessages)) {
			return false;
		}
		if (getClass() != obj.getClass())
			return false;
		ChatMessages other = (ChatMessages) obj;
		return messageId == other.messageId;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(messageId);
	}

}

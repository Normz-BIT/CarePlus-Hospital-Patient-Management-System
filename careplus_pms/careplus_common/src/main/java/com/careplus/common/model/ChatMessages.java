package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Chat Messages for all users
 *
 * A single message in the patient to staff conversation, carrying who sent it,
 * what they said, when, and whether it has been read. The read flag is what lets
 * the interface show a patient that staff have seen their message.
 *
 * A wire type for now, gaining its JPA mapping when ChatService is completed.
 *
 * TODO: add a recipient field. The model records only a sender at present, and
 * the recipient is passed separately by the controllers, so a stored message
 * does not yet carry who it was addressed to.
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

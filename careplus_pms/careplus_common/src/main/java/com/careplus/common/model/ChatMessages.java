package com.careplus.common.model;

import java.io.Serializable;
import java.util.Date;

/*
 * Chat Messages for all users
 * */

public class ChatMessages implements Serializable {
	private static final long serialVersionUID = 1L;

	private int messageId;
	private String senderId;
	private String content;
	private Date timeStamp;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Person)) {
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

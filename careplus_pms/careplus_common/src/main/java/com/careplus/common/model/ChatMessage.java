package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
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

@Entity
@Table(name = "chat_message")
public class ChatMessage implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id", nullable = false)
	private Integer messageId;
	/*
	 * A String matching Person.personId, so patients and staff share one identifier
	 * space and either can be a sender. Note ChatService.poll takes an int, an
	 * inconsistency that has to be resolved before the two can be wired together.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private String senderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private String receiverId;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;
	/*
	 * Orders the conversation, and is also what any operating hours rule would be
	 * evaluated against. That check belongs on the server rather than here, since a
	 * client clock can be wrong or deliberately altered.
	 */
	@Column(name = "sent_at", nullable = false)
	private LocalDateTime sentAt = LocalDateTime.now();
	/*
	 * Boxed rather than primitive, so null means "never set" as distinct from an
	 * explicit unread. Callers should treat null as unread rather than assume it is
	 * populated.
	 */
	  @Column(name = "is_read", nullable = false)
	    private Boolean isRead = false;

	  public ChatMessage() {
	    }

	  public ChatMessage(String sender, String receiver, String content, LocalDateTime sentAt, Boolean isRead) {
	        this.senderId = sender;
	        this.receiverId = receiver;
	        this.content = content;
	        this.sentAt = sentAt;
	        this.isRead = isRead;
	    }

	    public Integer getMessageId() {
	        return messageId;
	    }

	    public void setMessageId(Integer messageId) {
	        this.messageId = messageId;
	    }

	    public String getSenderId() {
	        return senderId;
	    }

	    public void setSenderId(String sender) {
	        this.senderId = sender;
	    }

	    public String getReceiver() {
	        return receiverId;
	    }

	    public void setReceiver(String receiver) {
	        this.receiverId = receiver;
	    }

	    public String getContent() {
	        return content;
	    }

	    public void setContent(String content) {
	        this.content = content;
	    }

	    public LocalDateTime getSentAt() {
	        return sentAt;
	    }

	    public void setSentAt(LocalDateTime sentAt) {
	        this.sentAt = sentAt;
	    }

	    public Boolean getIsRead() {
	        return isRead;
	    }

	    public void setIsRead(Boolean isRead) {
	        this.isRead = isRead;
	    }

	   
	    @Override
		public String toString() {
			return "ChatMessage [messageId=" + messageId + ", senderId=" + senderId + ", receiverId=" + receiverId
					+ ", content=" + content + ", sentAt=" + sentAt + ", isRead=" + isRead + "]";
		}

		@Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (!(obj instanceof ChatMessage)) return false;
	        ChatMessage other = (ChatMessage) obj;
	        return messageId != null && messageId.equals(other.messageId);
	    }

	    @Override
	    public int hashCode() {
	        return messageId == null ? 0 : messageId.hashCode();
	    }

}

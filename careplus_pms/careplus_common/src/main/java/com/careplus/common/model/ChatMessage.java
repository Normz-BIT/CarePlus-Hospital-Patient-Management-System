package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
/*
 * Chat Messages for all users
 *
 * One message in a patient/staff conversation: who sent it, what they said, when,
 * and whether it's been read. The read flag is what lets us show a patient that
 * staff have actually seen their message.
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
	 * Both are person_id Strings, so patients and staff share one set of IDs and
	 * either end can be the sender.
	 *
	 * Plain @Column rather than @ManyToOne, same reasoning as Payment, plus loading
	 * a whole Person for each end of a conversation would carry their password
	 * along with it. fk_chat_sender and fk_chat_receiver enforce both in the
	 * database.
	 *
	 * These used to have @ManyToOne on them while typed as Strings, which isn't a
	 * legal mapping at all: an association has to point at an entity, and String
	 * obviously isn't one.
	 */
	@Column(name = "sender_id", nullable = false)
	private String senderId;

	@Column(name = "receiver_id", nullable = false)
	private String receiverId;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;
	/*
	 * Puts the conversation in order. Note this comes off the client's clock, which
	 * is exactly why the 8-to-7 opening hours check lives in ChatService on the
	 * server and not here: a client's clock can be wrong or changed on purpose.
	 */
	@Column(name = "sent_at", nullable = false)
	private LocalDateTime sentAt = LocalDateTime.now();
	/*
	 * Boolean rather than boolean, so null means "never set" as opposed to a real
	 * unread. Treat null as unread rather than assuming it's always filled in.
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

	    public String getReceiverId() {
	        return receiverId;
	    }

	    public void setReceiverId(String receiverId) {
	        this.receiverId = receiverId;
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

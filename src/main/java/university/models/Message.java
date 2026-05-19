package university.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message implements Serializable {

    private String messageId;
    private User sender;
    private User recipient;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;

    public Message(String messageId, User sender, User recipient, String content, LocalDateTime sentAt) {
        this.messageId = messageId;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public String getMessageId() { return messageId; }
    public User getSender() { return sender; }
    public User getRecipient() { return recipient; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isRead() { return isRead; }

    @Override
    public String toString() {
        return "[" + (isRead ? "READ" : "UNREAD") + "] From: " + sender.getFirstName() + " | " + sentAt + "\n  " + content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        return Objects.equals(messageId, ((Message) o).messageId);
    }

    @Override
    public int hashCode() { return Objects.hash(messageId); }
}

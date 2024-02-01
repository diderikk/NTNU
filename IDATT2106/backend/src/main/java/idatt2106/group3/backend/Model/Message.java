package idatt2106.group3.backend.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import idatt2106.group3.backend.Model.DTO.MessageDTO;

import java.time.LocalDateTime;

/**
 * Entity class for storing information about a message
 * with messageId as primary key
 */
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    private String message;
    private LocalDateTime timeSent;

    @ManyToOne
    @JoinColumn(name = "chatId", referencedColumnName = "chatId")
    private Chat chat;

    @JsonIgnoreProperties({"messages","reportsSent","reportsReceived","activities","session","faults","role","rating","score","salt","hash","email","surname","organizedActivities"})
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    public Message(String message, LocalDateTime timeSent, Chat chat, User user) {
        this.message = message;
        this.timeSent = timeSent;
        this.chat = chat;
        this.user = user;
    }

    public Message(String message, LocalDateTime timeSent) {
        this.message = message;
        this.timeSent = timeSent;
    }

    public Message(MessageDTO messageDTO, Chat chat, User user){
        this.message = messageDTO.getMessage();
        this.timeSent = messageDTO.getTime();
        this.chat = chat;
        this.user = user;

    }

    public Message() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "messageId=" + messageId +
                ", message='" + message + '\'' +
                ", timeSent=" + timeSent +
                ", chat=" + chat +
                ", user=" + user +
                '}';
    }
}

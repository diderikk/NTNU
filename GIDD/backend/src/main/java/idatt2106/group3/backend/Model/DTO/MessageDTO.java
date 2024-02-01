package idatt2106.group3.backend.Model.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import idatt2106.group3.backend.Model.Message;

/**
 * DTO class for messages
 */
public class MessageDTO {
    private long userId;
    private String forename;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
    private String message;
    private long chatId;

    public MessageDTO() {}

    public MessageDTO(Message message){
        this.userId = message.getUser().getUserId();
        this.forename = message.getUser().getForename();
        this.time = message.getTimeSent();
        this.message = message.getMessage();
        this.chatId = message.getChat().getChatId();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}

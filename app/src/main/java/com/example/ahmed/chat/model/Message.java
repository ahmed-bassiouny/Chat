package com.example.ahmed.chat.model;

/**
 * Created by ahmed on 22/09/17.
 */

public class Message {
    private String messageText;
    private String fromId;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}

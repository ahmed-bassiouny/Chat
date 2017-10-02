package com.example.ahmed.chat.model;

import java.util.ArrayList;

/**
 * Created by ahmed on 22/09/17.
 */

public class Session {

    private String firstPerson;
    private String secondPerson;
    private ArrayList<Message> messages;

    public String getFirstPerson() {
        return firstPerson;
    }

    public void setFirstPerson(String firstPerson) {
        this.firstPerson = firstPerson;
    }

    public String getSecondPerson() {
        return secondPerson;
    }

    public void setSecondPerson(String secondPerson) {
        this.secondPerson = secondPerson;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}

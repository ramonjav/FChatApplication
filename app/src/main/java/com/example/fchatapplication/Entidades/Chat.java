package com.example.fchatapplication.Entidades;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;

public class Chat implements Serializable {

    private String sender;
    private String reciver;
    private String message;
    private String id;
    private Object createTimeStamp;
    private boolean isseen;

    public Chat() {

    }

    public Chat(String sender, String reciver, String message, String id, boolean isseen) {

        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
        this.id = id;
        this.isseen = isseen;
        createTimeStamp = ServerValue.TIMESTAMP;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Object createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}

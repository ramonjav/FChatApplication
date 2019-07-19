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
    private boolean isImage;
    private String urlFoto;
    private String NameFoto;

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

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNameFoto() {
        return NameFoto;
    }

    public void setNameFoto(String nameFoto) {
        NameFoto = nameFoto;
    }
}

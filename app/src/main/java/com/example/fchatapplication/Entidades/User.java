package com.example.fchatapplication.Entidades;

public class User {

    private  String id;
    private String Email;
    private String Name;
    private boolean Status;

    public User() {

    }

    public User(String id, String email, String name, boolean Status) {
        this.id = id;
        this.Email = email;
        this.Name = name;
        this.Status = Status;

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}

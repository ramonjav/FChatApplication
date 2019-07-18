package com.example.fchatapplication.Entidades;

public class User {

    private  String id;
    private String Email;
    private String Name;

    public User() {

    }

    public User(String id, String email, String name) {
        this.id = id;
        Email = email;
        Name = name;
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
}

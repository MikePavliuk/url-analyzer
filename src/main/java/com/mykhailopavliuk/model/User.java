package com.mykhailopavliuk.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String email;
    private char[] password;
    private List<?> urls = new ArrayList<>();

    public User() {
    }

    public User(long id, String email, char[] password, List<?> urls) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.urls = urls;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public List<?> getUrls() {
        return urls;
    }

    public void setUrls(List<?> urls) {
        this.urls = urls;
    }
}

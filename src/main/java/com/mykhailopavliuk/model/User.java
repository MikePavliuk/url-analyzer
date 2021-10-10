package com.mykhailopavliuk.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class User {
    private long id;
    private String email;
    private char[] password;
    private List<?> urls = new ArrayList<>();

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Arrays.equals(password, user.password) && Objects.equals(urls, user.urls);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, urls);
        result = 31 * result + Arrays.hashCode(password);
        return result;
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

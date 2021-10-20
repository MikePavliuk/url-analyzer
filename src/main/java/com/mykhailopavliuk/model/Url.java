package com.mykhailopavliuk.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Url {
    private long id;
    private String path;
    private List<Duration> responseTimes = new ArrayList<>();
    private User owner;

    public Url() {
    }

    public Url(long id, String path) {
        this.id = id;
        this.path = path;
    }

    public Url(long id, String path, List<Duration> responseTimes, User owner) {
        this.id = id;
        this.path = path;
        this.responseTimes = responseTimes;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Duration> getResponseTimes() {
        return responseTimes;
    }

    public void setResponseTimes(List<Duration> responseTimes) {
        this.responseTimes = responseTimes;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Url url = (Url) o;

        if (id != url.id) return false;
        return path.equals(url.path);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + path.hashCode();
        return result;
    }
}

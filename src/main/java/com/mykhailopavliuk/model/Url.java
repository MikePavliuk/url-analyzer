package com.mykhailopavliuk.model;

public class Url {
    private long id;
    private String path;
    private User owner;

    public Url() {
    }

    public Url(long id, String path) {
        this.id = id;
        this.path = path;
    }

    public Url(long id, String path, User owner) {
        this.id = id;
        this.path = path;
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

        return path.equals(url.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}

package com.mykhailopavliuk.model;

public class UserUrl {
    private long id;
    private long userId;
    private long urlId;

    public UserUrl() {
    }

    public UserUrl(long userId, long urlId) {
        this.userId = userId;
        this.urlId = urlId;
    }

    public UserUrl(long id, long userId, long urlId) {
        this.id = id;
        this.userId = userId;
        this.urlId = urlId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUrlId() {
        return urlId;
    }

    public void setUrlId(long urlId) {
        this.urlId = urlId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserUrl userUrl = (UserUrl) o;

        if (id != userUrl.id) return false;
        if (userId != userUrl.userId) return false;
        return urlId == userUrl.urlId;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (urlId ^ (urlId >>> 32));
        return result;
    }
}

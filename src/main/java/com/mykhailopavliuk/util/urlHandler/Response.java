package com.mykhailopavliuk.util.urlHandler;

import com.mykhailopavliuk.model.Url;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Response {
    private final Url url;
    private LocalDateTime startTime;
    private Duration duration;
    public static final String SEPARATOR = ";";

    public Response(Url url) {
        this.url = url;
    }

    public Response(Url url, LocalDateTime startTime) {
        this.url = url;
        this.startTime = startTime;
    }

    public Response(Url url, LocalDateTime startTime, Duration duration) {
        this.url = url;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Url getUrl() {
        return url;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (!url.equals(response.url)) return false;
        if (!startTime.equals(response.startTime)) return false;
        return Objects.equals(duration, response.duration);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  url.getId() + SEPARATOR +
                url.getPath() + SEPARATOR +
                startTime + SEPARATOR +
                (duration == null ? "-" : duration.toString());
    }
}

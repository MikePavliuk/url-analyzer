package com.mykhailopavliuk.util.urlHandler;

import com.mykhailopavliuk.model.Url;

import java.time.Duration;
import java.util.List;

public class PingStatistics {
    private Url url;
    private int totalNumberOfRequests;
    private int numberOfNotTimeoutResponses;
    private Duration fastestResponseTime = Duration.ofSeconds(5);
    private Duration slowestResponseTime = Duration.ofSeconds(0);
    private final Duration averageResponseTime;

    public PingStatistics(List<Response> responses) {
        double sumOfDurations = 0;
        numberOfNotTimeoutResponses = 0;
        for (Response response : responses) {
            if (response.getDuration() != null) {
                if (response.getDuration().compareTo(fastestResponseTime) < 0) {
                    fastestResponseTime = response.getDuration();
                }

                if (response.getDuration().compareTo(slowestResponseTime) > 0) {
                    slowestResponseTime = response.getDuration();
                }

                sumOfDurations += response.getDuration().toMillis();
            } else {
                numberOfNotTimeoutResponses++;
            }
            totalNumberOfRequests++;
        }
        averageResponseTime = Duration.ofMillis(Double.valueOf(sumOfDurations / (totalNumberOfRequests - numberOfNotTimeoutResponses)).longValue());
    }

    public PingStatistics(Url url, List<Response> responses) {
        this(responses);
        this.url = url;
    }

    public Url getUrl() {
        return url;
    }

    public int getTotalNumberOfRequests() {
        return totalNumberOfRequests;
    }

    public int getNumberOfNotTimeoutResponses() {
        return numberOfNotTimeoutResponses;
    }

    public Duration getFastestResponseTime() {
        return fastestResponseTime;
    }

    public Duration getSlowestResponseTime() {
        return slowestResponseTime;
    }

    public Duration getAverageResponseTime() {
        return averageResponseTime;
    }
}

package com.mykhailopavliuk.util.urlHandler;

import com.mykhailopavliuk.model.Url;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class HttpPing implements Callable<Response> {
    private final Url url;

    public HttpPing(Url url) {
        this.url = url;
    }

    @Override
    public Response call() {
        Duration duration = null;
        final LocalDateTime start = LocalDateTime.now();
        Response response = new Response(url, start);
        try {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(UrlHandler.getTimeoutInSeconds() * 1000)
                    .setConnectionRequestTimeout(UrlHandler.getTimeoutInSeconds() * 1000)
                    .setSocketTimeout(UrlHandler.getTimeoutInSeconds() * 1000).build();
            CloseableHttpClient client =
                    HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet request = new HttpGet(url.getPath());
            client.execute(request);
            final LocalDateTime stop = LocalDateTime.now();
            duration = Duration.between(start, stop);
            response.setDuration(duration);
        } catch (IOException ignored) {}
        return response;
    }
}

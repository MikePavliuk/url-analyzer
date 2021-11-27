package com.mykhailopavliuk.util.urlHandler;

import com.mykhailopavliuk.model.Url;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PingTask implements Runnable{
    private final List<Url> urls;
    private final Path outputPath;

    public PingTask(List<Url> urls, Path outputPath) {
        this.urls = urls;
        this.outputPath = outputPath;
    }

    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<HttpPing> calls = new ArrayList<>();
        for (Url url : urls) {
            calls.add(new HttpPing(url));
        }
        try {
            List<Future<Response>> futures = service.invokeAll(calls);
            BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardOpenOption.APPEND);
            for (Future<Response> future : futures) {
                Response response = null;
                try {
                    response = future.get(UrlHandler.getTimeoutInSeconds(), TimeUnit.SECONDS);
                } catch (TimeoutException | InterruptedException | ExecutionException ignored) {}
                if (response != null) {
                    writer.write(response + System.lineSeparator());
                }
            }
            writer.close();
            service.shutdownNow();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.mykhailopavliuk.util.urlHandler;

import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class UrlHandler {
    private static final int timeoutInSeconds = 5;
    private static boolean isAnalysingResponseTimes = false;
    private static Path outputFilePath;
    private static ScheduledExecutorService scheduledExecutorService;
    private static SettingsService settingsService;

    @Autowired
    public UrlHandler(SettingsService settingsService) {
        UrlHandler.settingsService = settingsService;
    }


    public static boolean isIsAnalysingResponseTimes() {
        return isAnalysingResponseTimes;
    }

    public static void setIsAnalysingResponseTimes(boolean isAnalysingResponseTimes) {
        UrlHandler.isAnalysingResponseTimes = isAnalysingResponseTimes;
    }

    public static int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public static void startUrlAnalysis(User user) {
        isAnalysingResponseTimes = true;

        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        TimeUnit unit;
        switch (settingsService.read().getRequestsFrequency()) {
            case PER_DAY:
                unit = TimeUnit.DAYS;
                break;

            case PER_HOUR:
                unit = TimeUnit.HOURS;
                break;

            default:
                unit = TimeUnit.MINUTES;
                break;
        }
        scheduledExecutorService.scheduleWithFixedDelay(
                new PingTask(user.getUrls(), outputFilePath),
                0,
                1,
                unit);

    }

    public static void initializeAnalysisOutputFile(User user) throws IOException {
        Path directoryOutput = Path.of(System.getProperty("java.io.tmpdir")).resolve("Url Analyzer");
        Files.createDirectories(directoryOutput);
        outputFilePath = directoryOutput.resolve(user.getId() + "_ping.log");

        if (!Files.exists(outputFilePath)) {
            Files.createFile(outputFilePath);
        }

        deleteDataThatOlderThenPeriod(Period.ofDays(10));
    }

    private static void deleteDataThatOlderThenPeriod(Period period) {
        String line;
        String[] responseData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(outputFilePath)) {
            while ((line = reader.readLine()) != null) {
                responseData = line.split(Response.SEPARATOR);
                if (period.getDays() >= Period.between(LocalDateTime.parse(responseData[2]).toLocalDate(), LocalDate.now()).getDays()) {
                    updatedFile.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while deleting from the " + outputFilePath);
        }

        try (var writer = Files.newBufferedWriter(outputFilePath)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User database");
        }
    }

    public static void stopUrlAnalysis() {
        isAnalysingResponseTimes = false;
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }

    public static void deleteUrlFromLog(long urlId) {
        String line;
        String[] responseData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(outputFilePath)) {
            while ((line = reader.readLine()) != null) {
                responseData = line.split(Response.SEPARATOR);
                if (Long.parseLong(responseData[0]) != urlId) {
                    updatedFile.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while deleting from the " + outputFilePath);
        }

        try (var writer = Files.newBufferedWriter(outputFilePath)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User database");
        }
    }

    public static void deleteLogByUserId(long userId) {
        try {
            Files.deleteIfExists(Path.of(System.getProperty("java.io.tmpdir")).resolve("Url Analyzer").resolve(userId + "_ping.log"));
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while deleting log for User with id = " + userId);
        }
    }

    public static List<Response> getAllResponsesByUrlId(long urlId) {
        List<Response> responses = new ArrayList<>();
        if (outputFilePath == null) return responses;

        String line;
        String[] responseData;

        try (var reader = Files.newBufferedReader(outputFilePath)) {
            while ((line = reader.readLine()) != null) {
                responseData = line.split(Response.SEPARATOR);

                if (responseData[0].equals(String.valueOf(urlId))) {

                    Duration duration = null;
                    if (!Objects.equals(responseData[3], "-")) {
                        duration = Duration.parse(responseData[3]);
                    }

                    responses.add(new Response(
                            new Url(Long.parseLong(responseData[0]), responseData[1]),
                            LocalDateTime.parse(responseData[2]),
                            duration
                    ));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the " + outputFilePath);
        }

        return responses;
    }

    public static boolean isUrlPathExists(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setConnectTimeout((int) Duration.ofSeconds(timeoutInSeconds).toMillis());

        huc.setRequestMethod("HEAD");

        try {
            return huc.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND;
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException(e.getMessage());
        }
    }
}

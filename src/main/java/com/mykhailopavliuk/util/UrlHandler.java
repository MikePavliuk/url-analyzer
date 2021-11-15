package com.mykhailopavliuk.util;

import com.mykhailopavliuk.model.Url;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class UrlHandler {
    private static boolean isAnalysingResponseTimes = false;


    public static void startUrlAnalysis(List<Url> url, int frequency_per_day) {

    }


    public static boolean isUrlPathExists(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");

        return huc.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND;
    }

    public static boolean isIsAnalysingResponseTimes() {
        return isAnalysingResponseTimes;
    }

    public static void setIsAnalysingResponseTimes(boolean isAnalysingResponseTimes) {
        UrlHandler.isAnalysingResponseTimes = isAnalysingResponseTimes;
    }
}

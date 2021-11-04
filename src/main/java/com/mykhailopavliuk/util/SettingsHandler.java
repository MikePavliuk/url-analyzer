package com.mykhailopavliuk.util;

import com.mykhailopavliuk.model.Settings;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.nio.file.Paths;

public class SettingsHandler extends DefaultHandler {
    private Settings settings;
    private boolean bDisplayMode = false;
    private boolean bScreenResolution = false;
    private boolean bRequestsFrequency = false;
    private boolean bExportDirectory = false;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {

        if ("Settings".equals(qName)) {
            settings = new Settings();
        }

        switch (qName) {

            case "DisplayMode":
                bDisplayMode = true;
                break;

            case "ScreenResolution":
                bScreenResolution = true;
                break;

            case "RequestsFrequency":
                bRequestsFrequency = true;
                break;
            case "ExportDirectory":
                bExportDirectory = true;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

        if (bDisplayMode) {
            settings.setDisplayMode(Settings.DisplayMode.valueOf(new String(ch, start, length)));
            bDisplayMode = false;
        }

        if (bScreenResolution) {
            settings.setScreenResolution(Settings.ScreenResolution.valueOf(new String(ch, start, length)));
            bScreenResolution = false;
        }

        if (bRequestsFrequency) {
            settings.setRequestsFrequency(Settings.RequestsFrequency.valueOf(new String(ch, start, length)));
            bRequestsFrequency = false;
        }
        if (bExportDirectory) {
            settings.setExportDirectory(Paths.get(new String(ch, start, length)));
            bExportDirectory = false;
        }

    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) {

        if ("Settings".equals(qName)) {
            // тут нічого немає, бо в нас всього 1 елемент, а не список
        }
    }

    public Settings getSettings() {
        return settings;
    }
}

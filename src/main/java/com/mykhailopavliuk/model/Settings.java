package com.mykhailopavliuk.model;

import com.mykhailopavliuk.configuration.jaxb.PathAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.nio.file.Path;

@XmlRootElement(name = "Settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings implements Serializable {

    public enum DisplayMode {
        DARK, LIGHT
    }

    public enum ScreenResolution {
        SMALL, MEDIUM, LARGE
    }

    public enum RequestsFrequency {
        PER_MINUTE, PER_HOUR, PER_DAY
    }

    @XmlElement(name = "DisplayMode")
    private DisplayMode displayMode = DisplayMode.LIGHT;
    @XmlElement(name = "ScreenResolution")
    private ScreenResolution screenResolution = ScreenResolution.MEDIUM;
    @XmlElement(name = "RequestsFrequency")
    private RequestsFrequency requestsFrequency = RequestsFrequency.PER_HOUR;
    @XmlElement(name = "ExportDirectory")
    @XmlJavaTypeAdapter(value = PathAdapter.class)
    private Path exportDirectory = Path.of("");

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public ScreenResolution getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(ScreenResolution screenResolution) {
        this.screenResolution = screenResolution;
    }

    public RequestsFrequency getRequestsFrequency() {
        return requestsFrequency;
    }

    public void setRequestsFrequency(RequestsFrequency requestsFrequency) {
        this.requestsFrequency = requestsFrequency;
    }

    public Path getExportDirectory() {
        return exportDirectory;
    }

    public void setExportDirectory(Path exportDirectory) {
        this.exportDirectory = exportDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        if (displayMode != settings.displayMode) return false;
        if (screenResolution != settings.screenResolution) return false;
        if (requestsFrequency != settings.requestsFrequency) return false;
        return exportDirectory.equals(settings.exportDirectory);
    }

    @Override
    public int hashCode() {
        int result = displayMode.hashCode();
        result = 31 * result + screenResolution.hashCode();
        result = 31 * result + requestsFrequency.hashCode();
        result = 31 * result + exportDirectory.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "displayMode=" + displayMode +
                ", screenResolution=" + screenResolution +
                ", requestsFrequency=" + requestsFrequency.toString() +
                ", exportDirectory=" + exportDirectory +
                '}';
    }
}

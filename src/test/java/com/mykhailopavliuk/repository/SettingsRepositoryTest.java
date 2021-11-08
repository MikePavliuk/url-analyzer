package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.TestConfig;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.model.UserUrl;
import com.mykhailopavliuk.repository.impl.SettingsRepositoryImpl;
import com.mykhailopavliuk.repository.impl.UrlRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles({ "test" })
public class SettingsRepositoryTest {

    @Autowired
    private SettingsRepositoryImpl settingsRepository;

    @Autowired
    @Qualifier("settings")
    private Path ordinarySettings;

    private Settings settings;

    @BeforeEach
    public void setUp() {
        settings = new Settings();
        settings.setDisplayMode(Settings.DisplayMode.LIGHT);
        settings.setScreenResolution(Settings.ScreenResolution.MEDIUM);
        settings.setRequestsFrequency(Settings.RequestsFrequency.PER_HOUR);
        settings.setExportDirectory(Path.of("").toAbsolutePath());
    }

    @AfterEach
    public void tearDown() {
        settings = null;
        settingsRepository.delete();
    }

    @Test
    public void testSaveSettings() {
        Settings expected = settingsRepository.save(settings);
        Settings actual = settingsRepository.find();

        assertEquals(expected, actual);
    }

    @Test
    public void testFindSettingsIfValid() {
        Settings expected = settingsRepository.save(settings);
        Settings actual = settingsRepository.find();

        assertEquals(expected, actual);
    }

    @Test
    public void testFindSettingsIfNotValid() {
        Settings actual = settingsRepository.find();

        settings.setRequestsFrequency(Settings.RequestsFrequency.PER_MINUTE);
        settings.setExportDirectory(Path.of("/"));
        Settings expected = settingsRepository.save(settings);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteSettings() {
        settingsRepository.save(settings);
        settingsRepository.delete();

        assertFalse(Files.exists(ordinarySettings));
    }


}

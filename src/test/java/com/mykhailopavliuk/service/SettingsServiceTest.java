package com.mykhailopavliuk.service;

import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.exception.NullEntityReferenceException;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.repository.SettingsRepository;
import com.mykhailopavliuk.service.impl.SettingsServiceImpl;
import com.mykhailopavliuk.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class SettingsServiceTest {

    @Mock
    private SettingsRepository settingsRepository;

    @InjectMocks
    private SettingsServiceImpl settingsService;

    private Settings expected;

    @BeforeEach
    public void setUp() {
        expected = new Settings();
        expected.setDisplayMode(Settings.DisplayMode.LIGHT);
        expected.setScreenResolution(Settings.ScreenResolution.MEDIUM);
        expected.setRequestsFrequency(Settings.RequestsFrequency.PER_HOUR);
        expected.setExportDirectory(Path.of("").toAbsolutePath());
    }

    @AfterEach
    public void tearDown() {
        expected = null;
    }

    @Test
    public void testCorrectSave() {
        when(settingsRepository.save(expected)).thenReturn(expected);
        Settings actual = settingsService.save(expected);

        assertEquals(expected, actual);
        verify(settingsRepository, times(1)).save(expected);
    }

    @Test
    public void testExceptionCreate() {
        Exception exception = assertThrows(NullEntityReferenceException.class,
                () -> settingsService.save(null)
        );

        assertEquals("Settings cannot be 'null'", exception.getMessage());
        verify(settingsRepository, never()).save(any(Settings.class));
    }

    @Test
    public void testCorrectRead() {
        when(settingsRepository.find()).thenReturn(expected);
        Settings actual = settingsService.read();

        assertEquals(expected, actual);
        verify(settingsRepository, times(1)).find();
    }

    @Test
    public void testReadWithUndefinedPath() {
        when(settingsRepository.find()).thenReturn(expected);
        expected.setExportDirectory(Path.of("/lalalal"));
        Settings actual = settingsService.read();
        Settings newExpected = expected;
        expected.setExportDirectory(Path.of(System.getProperty("user.home")).toAbsolutePath());

        assertEquals(newExpected, actual);
        verify(settingsRepository, times(1)).find();
        verify(settingsRepository, times(1)).save(any(Settings.class));
    }

}

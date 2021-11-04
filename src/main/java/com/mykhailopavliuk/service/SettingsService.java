package com.mykhailopavliuk.service;

import com.mykhailopavliuk.exception.SettingsException;
import com.mykhailopavliuk.model.Settings;

import java.util.Optional;

public interface SettingsService {
    Settings save(Settings setting);
    Settings read();
}

package com.mykhailopavliuk.service;

import com.mykhailopavliuk.model.Settings;

public interface SettingsService {
    Settings save(Settings setting);

    Settings read();
}

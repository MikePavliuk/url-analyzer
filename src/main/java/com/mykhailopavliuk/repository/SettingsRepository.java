package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.model.Settings;

public interface SettingsRepository {
    Settings save(Settings settings);
    Settings find();
    void delete();
}

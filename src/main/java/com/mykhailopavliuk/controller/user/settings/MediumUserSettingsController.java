package com.mykhailopavliuk.controller.user.settings;

import com.mykhailopavliuk.service.SettingsService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/settings/medium-settings.fxml")
public class MediumUserSettingsController extends UserSettingsController {

    @Autowired
    public MediumUserSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        super(fxWeaver, settingsService);
    }

}

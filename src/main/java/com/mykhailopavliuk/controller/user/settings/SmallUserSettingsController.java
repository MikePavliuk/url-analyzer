package com.mykhailopavliuk.controller.user.settings;

import com.mykhailopavliuk.service.SettingsService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/settings/small-settings.fxml")
public class SmallUserSettingsController extends UserSettingsController {

    @Autowired
    public SmallUserSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        super(fxWeaver, settingsService);
    }

}

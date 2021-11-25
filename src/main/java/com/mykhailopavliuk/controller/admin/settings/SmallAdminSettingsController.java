package com.mykhailopavliuk.controller.admin.settings;

import com.mykhailopavliuk.service.SettingsService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/admin/settings/small-settings.fxml")
public class SmallAdminSettingsController extends AdminSettingsController {

    @Autowired
    public SmallAdminSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        super(fxWeaver, settingsService);
    }

}

package com.mykhailopavliuk.controller.admin.settings;

import com.mykhailopavliuk.service.SettingsService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/admin/settings/large-settings.fxml")
public class LargeAdminSettingsController extends AdminSettingsController {

    @Autowired
    public LargeAdminSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        super(fxWeaver, settingsService);
    }

}

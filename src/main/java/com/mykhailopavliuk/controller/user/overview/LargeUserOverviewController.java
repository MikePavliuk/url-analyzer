package com.mykhailopavliuk.controller.user.overview;

import com.mykhailopavliuk.service.SettingsService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/overview/large-overview.fxml")
public class LargeUserOverviewController extends UserOverviewController {

    @Autowired
    public LargeUserOverviewController(FxWeaver fxWeaver, SettingsService settingsService) {
        super(fxWeaver, settingsService);
    }
}

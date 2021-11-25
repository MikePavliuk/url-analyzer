package com.mykhailopavliuk.controller.admin.overview;

import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UrlService;
import com.mykhailopavliuk.service.UserService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/admin/overview/small-overview.fxml")
public class SmallAdminOverviewController extends AdminOverviewController {

    @Autowired
    public SmallAdminOverviewController(FxWeaver fxWeaver, UserService userService, UrlService urlService, SettingsService settingsService) {
        super(fxWeaver, userService, urlService, settingsService);
    }

}

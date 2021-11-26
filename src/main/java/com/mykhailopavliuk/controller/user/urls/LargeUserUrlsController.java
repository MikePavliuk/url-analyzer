package com.mykhailopavliuk.controller.user.urls;

import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UrlService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/urls/large-urls.fxml")
public class LargeUserUrlsController extends UserUrlsController {

    @Autowired
    public LargeUserUrlsController(FxWeaver fxWeaver, UrlService urlService, SettingsService settingsService) {
        super(fxWeaver, urlService, settingsService);
    }

}

package com.mykhailopavliuk.controller.user.urls;

import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UrlService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/urls/small-urls.fxml")
public class SmallUserUrlsController extends UserUrlsController {

    @Autowired
    public SmallUserUrlsController(FxWeaver fxWeaver, UrlService urlService, SettingsService settingsService) {
        super(fxWeaver, urlService, settingsService);
    }

}

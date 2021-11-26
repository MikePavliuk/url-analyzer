package com.mykhailopavliuk.controller.user.url;

import com.mykhailopavliuk.service.SettingsService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/url/medium-url.fxml")
public class MediumUserUrlController extends UserUrlController {

    @Autowired
    public MediumUserUrlController(FxWeaver fxWeaver, SettingsService settingsService) {
        super(fxWeaver, settingsService);
    }

}

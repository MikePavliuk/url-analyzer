package com.mykhailopavliuk.controller.user.overview;


import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/overview/medium-overview.fxml")
public class MediumUserOverviewController extends UserOverviewController {

    @Autowired
    public MediumUserOverviewController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }
}
package com.mykhailopavliuk.controller.user.overview;


import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/overview/small-overview.fxml")
public class SmallUserOverviewController extends UserOverviewController {

    @Autowired
    public SmallUserOverviewController(FxWeaver fxWeaver) {
        super(fxWeaver);
    }
}

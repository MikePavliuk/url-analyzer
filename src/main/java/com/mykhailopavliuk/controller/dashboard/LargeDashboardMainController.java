package com.mykhailopavliuk.controller.dashboard;

import com.mykhailopavliuk.service.UserService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/dashboard/large-dashboard-main.fxml")
public class LargeDashboardMainController extends DashboardMainController {

    @Autowired
    public LargeDashboardMainController(UserService userService, FxWeaver fxWeaver) {
        super(userService, fxWeaver);
    }
}

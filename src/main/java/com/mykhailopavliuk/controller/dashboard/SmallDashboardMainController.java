package com.mykhailopavliuk.controller.dashboard;


import com.mykhailopavliuk.service.UserService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/dashboard/small-dashboard-main.fxml")
public class SmallDashboardMainController extends DashboardMainController {

    @Autowired
    public SmallDashboardMainController(UserService userService, FxWeaver fxWeaver) {
        super(userService, fxWeaver);
    }
}

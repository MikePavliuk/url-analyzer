package com.mykhailopavliuk.controller.user.overview;


import com.mykhailopavliuk.service.UserService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/user/overview/small-overview.fxml")
public class SmallUserOverviewController extends UserOverviewController {

    @Autowired
    public SmallUserOverviewController(UserService userService, FxWeaver fxWeaver) {
        super(userService, fxWeaver);
    }
}

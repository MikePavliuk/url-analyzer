package com.mykhailopavliuk.controller.admin.users;

import com.mykhailopavliuk.configuration.application.AdminProperties;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UserService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/admin/users/small-users.fxml")
public class SmallAdminUsersController extends AdminUsersController {

    @Autowired
    public SmallAdminUsersController(UserService userService, SettingsService settingsService, AdminProperties adminProperties, FxWeaver fxWeaver) {
        super(userService, settingsService, adminProperties, fxWeaver);
    }

}

package com.mykhailopavliuk.dto;

import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.UserService;

import java.util.ArrayList;

public class UserTransformer {
    public static UserTableRowDTO convertToDto(User user) {
        return new UserTableRowDTO(
                user.getId(),
                user.getEmail(),
                user.getUrls().size()
        );
    }

    public static User convertToEntity(UserTableRowDTO userDto, UserService userService) {
        return new User(
                userDto.getId(),
                userDto.getEmail(),
                userService.readById(userDto.getId()).getPassword(),
                new ArrayList<>()
        );
    }
}

package com.mykhailopavliuk.dto;

import com.mykhailopavliuk.model.User;

public class UserTransformer {
    public static UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getPassword()
        );
    }

    public static User convertToEntity(UserDTO userDto) {
        User user = new User();

        return user;
    }
}

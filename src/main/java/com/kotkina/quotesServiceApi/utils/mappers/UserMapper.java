package com.kotkina.quotesServiceApi.utils.mappers;

import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.web.models.requests.UserRegisterRequest;
import com.kotkina.quotesServiceApi.web.models.responses.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User requestToUser(UserRegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public UserResponse userToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}

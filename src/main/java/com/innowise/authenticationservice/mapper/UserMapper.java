package com.innowise.authenticationservice.mapper;

import com.innowise.authenticationservice.model.dto.UserCreateDto;
import com.innowise.authenticationservice.model.dto.UserResponse;
import com.innowise.authenticationservice.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateDto userCreateDto);
    UserResponse toUserResponse(User user);
}

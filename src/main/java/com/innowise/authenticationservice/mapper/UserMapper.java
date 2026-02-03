package com.innowise.authenticationservice.mapper;

import com.innowise.authenticationservice.model.dto.UserDto;
import com.innowise.authenticationservice.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);

}

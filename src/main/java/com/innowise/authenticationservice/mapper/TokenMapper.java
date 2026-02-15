package com.innowise.authenticationservice.mapper;

import com.innowise.authenticationservice.model.dto.CreateTokenDto;
import com.innowise.authenticationservice.model.entity.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    RefreshToken toToken(CreateTokenDto createTokenDto);
}

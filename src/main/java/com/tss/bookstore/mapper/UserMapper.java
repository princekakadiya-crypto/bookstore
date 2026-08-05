package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.UserRequestDto;
import com.tss.bookstore.dto.UserResponseDto;
import com.tss.bookstore.dto.UserWithProfileResponse;
import com.tss.bookstore.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(UserRequestDto requestDto);
    UserWithProfileResponse toProfileDto(User user);
}

package com.tss.bookstore.mapper;

import com.tss.bookstore.dto.UserProfileResponseDto;
import com.tss.bookstore.dto.UserRequestDto;
import com.tss.bookstore.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto toDto(UserProfile userProfile);
    UserProfile toEntity(UserRequestDto requestDto);
}

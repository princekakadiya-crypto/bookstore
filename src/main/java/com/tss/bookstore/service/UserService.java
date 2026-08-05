package com.tss.bookstore.service;

import com.tss.bookstore.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponseDto addUser(UserRequestDto requestDto);
    UserResponseDto editUser(Long userUd,UserRequestDto requestDto);
    UserResponseDto getUserById(Long userId);
    PageDto getAllUser(Pageable pageable);
    void deleteUser(Long userId);

    UserProfileResponseDto getUserProfile(Long userId);
    UserWithProfileResponse getUserWithProfile(Long userId);

    PageDto<UserWithProfileResponse> getAllUserDetails(Pageable pageable);
}

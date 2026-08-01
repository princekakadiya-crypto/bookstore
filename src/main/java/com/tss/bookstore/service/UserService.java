package com.tss.bookstore.service;

import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.UserRequestDto;
import com.tss.bookstore.dto.UserResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponseDto addUser(UserRequestDto requestDto);
    UserResponseDto editUser(Long userUd,UserRequestDto requestDto);
    UserResponseDto getUserById(Long userId);
    PageDto getAllUser(Pageable pageable);
    void deleteUser(Long userId);
}

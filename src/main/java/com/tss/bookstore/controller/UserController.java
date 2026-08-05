package com.tss.bookstore.controller;

import com.tss.bookstore.dto.*;
import com.tss.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody UserRequestDto requestDto){
        return new ResponseEntity<>(
                userService.addUser(requestDto),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> editUser(@PathVariable Long userId,@Valid @RequestBody UserRequestDto requestDto){
        return new ResponseEntity<>(
                userService.editUser(userId,requestDto),
                HttpStatus.OK
        );
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId){
        return new ResponseEntity<>(
                userService.getUserById(userId),
                HttpStatus.OK
        );
    }

    @GetMapping("/users/details")
    public ResponseEntity<PageDto<UserWithProfileResponse>> getUsersDetails(Pageable pageable) {
        return ResponseEntity.ok(
                userService.getAllUserDetails(pageable)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<PageDto<UserResponseDto>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(
                userService.getAllUser(pageable)
        );
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(
                userService.getUserProfile(userId)
        );
    }

    @GetMapping("/users/{userId}/details")
    public ResponseEntity<UserWithProfileResponse> getUserWithProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(
                userService.getUserWithProfile(userId)
        );
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}

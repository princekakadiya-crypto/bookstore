package com.tss.bookstore.service;

import com.tss.bookstore.dto.*;
import com.tss.bookstore.entity.User;
import com.tss.bookstore.entity.UserProfile;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.UserMapper;
import com.tss.bookstore.mapper.UserProfileMapper;
import com.tss.bookstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserRepository userRepository;

    private static final Logger log= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto requestDto) {
        log.info("Creating user. email={}", requestDto.getEmail());

        if(userRepository.existsByEmailIgnoreCase(requestDto.getEmail())){
            log.info(
                    "Creating user. email={}",
                    requestDto.getEmail()
            );
            throw new DuplicateResourceException(
                    "Email already exists");
        }

        User user=userMapper.toEntity(requestDto);
        user.setUserProfile(userProfileMapper.toEntity(requestDto));
        User result=userRepository.save(user);

        UserResponseDto responseDto=userMapper.toDto(result);

        log.info(
                "User created successfully. userId={}, email={}",
                result.getUserId(),
                result.getEmail()
        );

        return responseDto;
    }

    @Override
    @Transactional
    public UserResponseDto editUser(Long userId,UserRequestDto requestDto) {
        log.info("Updating user. userId={}", userId);
        User user=userRepository.findByUserIdAndIsActiveTrue(userId).orElseThrow(
                ()-> new NotFoundException("User Not Found")
        );

        if (userRepository.existsByEmailAndUserIdNot(requestDto.getEmail(), userId)) {

            throw new DuplicateResourceException(
                    "Email already exists");
        }

        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        UserProfile profile = user.getUserProfile();

        profile.setPhone(requestDto.getPhone());
        profile.setAddress(requestDto.getAddress());
        profile.setDateOfBirth(requestDto.getDateOfBirth());
        profile.setAvatar(requestDto.getAvatar());

        User result = userRepository.save(user);

        log.info("User updated successfully. userId={}", userId);

        UserResponseDto responseDto=userMapper.toDto(result);

        return responseDto;
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        log.debug("Fetching user. userId={}", userId);
        User user=userRepository.findByUserIdAndIsActiveTrue(userId).orElseThrow(
                ()-> new NotFoundException("User Not Found")
        );
        UserResponseDto responseDto=userMapper.toDto(user);

        return responseDto;
    }

    @Override
    public PageDto<UserResponseDto> getAllUser(Pageable pageable) {

        log.debug(
                "Fetching users. page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<User> users = userRepository.findByIsActiveTrue(pageable);
        List<UserResponseDto> responseDtos = new ArrayList<>();

        for (User user : users.getContent()) {
            UserResponseDto dto = userMapper.toDto(user);
            responseDtos.add(dto);
        }

        PageDto<UserResponseDto> pageDto = new PageDto<>();

        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(users.getNumber());
        pageDto.setPageSize(users.getSize());
        pageDto.setTotalPages(users.getTotalPages());
        pageDto.setTotalElements(users.getTotalElements());
        pageDto.setFirst(users.isFirst());
        pageDto.setLast(users.isLast());
        pageDto.setEmpty(users.isEmpty());

        return pageDto;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Soft deleting user. userId={}", userId);
        User user = userRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id : " + userId));
        user.setActive(false);
        userRepository.save(user);
        log.info("User deleted successfully. userId={}", userId);
    }

    @Override
    public UserProfileResponseDto getUserProfile(Long userId) {
        log.debug("Fetching user profile. userId={}", userId);

        User user = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id : " + userId));
        return userProfileMapper.toDto(user.getUserProfile());
    }

    @Override
    public UserWithProfileResponse getUserWithProfile(Long userId) {
        log.debug("Fetching user with profile. userId={}", userId);

        User user = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id : " + userId));

        UserWithProfileResponse response=userMapper.toProfileDto(user);
        response.setProfile(userProfileMapper.toDto(user.getUserProfile()));

        return response;
    }

    @Override
    public PageDto<UserWithProfileResponse> getAllUserDetails(Pageable pageable) {
        log.debug(
                "Fetching users. page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<User> users = userRepository.findActiveUsersWithProfile(pageable);
        List<UserWithProfileResponse> responseDtos = new ArrayList<>();

        for (User user : users.getContent()) {
            UserWithProfileResponse dto = userMapper.toProfileDto(user);
            dto.setProfile(userProfileMapper.toDto(user.getUserProfile()));
            responseDtos.add(dto);
        }

        PageDto<UserWithProfileResponse> pageDto = new PageDto<>();

        pageDto.setContent(responseDtos);
        pageDto.setCurrentPage(users.getNumber());
        pageDto.setPageSize(users.getSize());
        pageDto.setTotalPages(users.getTotalPages());
        pageDto.setTotalElements(users.getTotalElements());
        pageDto.setFirst(users.isFirst());
        pageDto.setLast(users.isLast());
        pageDto.setEmpty(users.isEmpty());

        return pageDto;
    }
}

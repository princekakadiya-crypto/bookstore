package com.tss.bookstore.service;

import com.tss.bookstore.dto.PageDto;
import com.tss.bookstore.dto.UserRequestDto;
import com.tss.bookstore.dto.UserResponseDto;
import com.tss.bookstore.entity.User;
import com.tss.bookstore.entity.UserProfile;
import com.tss.bookstore.exception.DuplicateResourceException;
import com.tss.bookstore.exception.NotFoundException;
import com.tss.bookstore.mapper.UserMapper;
import com.tss.bookstore.mapper.UserProfileMapper;
import com.tss.bookstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto requestDto) {

        if(userRepository.existsByEmailIgnoreCase(requestDto.getEmail())){
            throw new DuplicateResourceException(
                    "Email already exists");
        }

        User user=userMapper.toEntity(requestDto);
        user.setUserProfile(userProfileMapper.toEntity(requestDto));
        User result=userRepository.save(user);

        UserResponseDto responseDto=userMapper.toDto(result);
        responseDto.setProfile(userProfileMapper.toDto(result.getUserProfile()));

        return responseDto;
    }

    @Override
    @Transactional
    public UserResponseDto editUser(Long userId,UserRequestDto requestDto) {
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

        UserResponseDto responseDto=userMapper.toDto(result);
        responseDto.setProfile(userProfileMapper.toDto(result.getUserProfile()));

        return responseDto;
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user=userRepository.findByUserIdAndIsActiveTrue(userId).orElseThrow(
                ()-> new NotFoundException("User Not Found")
        );
        UserResponseDto responseDto=userMapper.toDto(user);
        responseDto.setProfile(userProfileMapper.toDto(user.getUserProfile()));

        return responseDto;
    }

    @Override
    public PageDto<UserResponseDto> getAllUser(Pageable pageable) {

        Page<User> users = userRepository.findByIsActiveTrue(pageable);
        List<UserResponseDto> responseDtos = new ArrayList<>();

        for (User user : users.getContent()) {
            UserResponseDto dto = userMapper.toDto(user);
            dto.setProfile(userProfileMapper.toDto(user.getUserProfile()));
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

        User user = userRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id : " + userId));
        user.setActive(false);
        userRepository.save(user);
    }
}

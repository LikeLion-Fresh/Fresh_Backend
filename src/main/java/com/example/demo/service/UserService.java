package com.example.demo.service;

import com.example.demo.dto.UserCreateDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void updateUserRole(String username, String newRole) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
        }
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Transactional
    public Long createUser(UserCreateDto userCreateDto) {
        User user = User.builder()
                .username(userCreateDto.getUsername())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .email(userCreateDto.getEmail())
                .role("ROLE_USER")
                .build();
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public UserResponseDto getUserInfo(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getProvider(),
                user.getProviderId()
        );
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
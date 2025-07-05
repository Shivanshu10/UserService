package com.shivanshu.secexample.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivanshu.secexample.dao.UserRepository;
import com.shivanshu.secexample.dto.BaseDTO;
import com.shivanshu.secexample.dto.UserDTO;
import com.shivanshu.secexample.entity.User;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public BaseDTO<List<UserDTO>> getAllUsers() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            return BaseDTO.<List<UserDTO>>builder()
                    .message("No users found")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build();
        }

        List<UserDTO> userDTOList = userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return BaseDTO.<List<UserDTO>>builder()
                .message("Users found successfully")
                .statusCode(HttpStatus.OK)
                .data(userDTOList)
                .build();
    }

    public BaseDTO<UserDTO> getUserById(Long id) {
        if (id == null) {
            return BaseDTO.<UserDTO>builder()
                    .message("User ID cannot be null")
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build();
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return BaseDTO.<UserDTO>builder()
                    .message("User not found")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build();
        }

        UserDTO userDTO = modelMapper.map(user.get(), UserDTO.class);
        return BaseDTO.<UserDTO>builder()
                .message("User found successfully")
                .statusCode(HttpStatus.OK)
                .data(userDTO)
                .build();
    }

    public BaseDTO<UserDTO> createUser(UserDTO userDTO) {
        if (userDTO == null) {
            return BaseDTO.<UserDTO>builder()
                    .message("User ID cannot be null")
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build();
        }

        // check if username already exists
        if (userRepository.existsByUsername(userDTO.getUsername()) || (userDTO.getId() != null && userRepository.existsById(userDTO.getId()))) {
            return BaseDTO.<UserDTO>builder()
                    .message("Username already exists")
                    .statusCode(HttpStatus.CONFLICT)
                    .data(null)
                    .build();
        }

        try {
            User user = modelMapper.map(userDTO, User.class);
            user = userRepository.save(user);

            UserDTO respUserDTO = modelMapper.map(user, UserDTO.class);
            return BaseDTO.<UserDTO>builder()
                    .message("User created successfully")
                    .statusCode(HttpStatus.CREATED)
                    .data(respUserDTO)
                    .build();
        } catch (Exception e) {
            return BaseDTO.<UserDTO>builder()
                    .message("Error creating user: " + e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build();
        }
    }

    public BaseDTO<UserDTO> updateUser(Long id, Map<String, Object> updates) {
        if (id == null || updates == null) {
            return BaseDTO.<UserDTO>builder()
                    .message("No user data provided")
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build();
        }

        // check if userId exists
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return BaseDTO.<UserDTO>builder()
                    .message("User with id " + id + " does not exist")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build();
        }
        User user = userOptional.get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        try {
            objectMapper.updateValue(userDTO, updates);
        } catch (JsonMappingException e) {
            return BaseDTO.<UserDTO>builder()
                    .message("Invalid patch data: " + e.getOriginalMessage())
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build();
        }

        // check if username already exists
        if (user.getUsername() != userDTO.getUsername() && userRepository.existsByUsername(userDTO.getUsername())) {
            return BaseDTO.<UserDTO>builder()
                    .message("Username already exists")
                    .statusCode(HttpStatus.CONFLICT)
                    .data(null)
                    .build();
        }

        user = modelMapper.map(userDTO, User.class);

        userRepository.save(user);
    
        return BaseDTO.<UserDTO>builder()
                .message("User updated successfully")
                .statusCode(HttpStatus.OK)
                .data(userDTO)
                .build();
    }

    public BaseDTO<Long> deleteUser(Long id) {
        if (id == null) {
            return BaseDTO.<Long>builder()
                    .message("User ID cannot be null")
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build();
        }

        // handle case where user does not exist
        if (!userRepository.existsById(id)) {
            return BaseDTO.<Long>builder()
                    .message("User with id " + id + " does not exist")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .data(null)
                    .build();
        }

        userRepository.deleteById(id);
        return BaseDTO.<Long>builder()
                .message("User deleted successfully")
                .statusCode(HttpStatus.OK)
                .data(id)
                .build();
    }
}

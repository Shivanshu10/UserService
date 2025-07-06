package com.shivanshu.secexample.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.shivanshu.secexample.dto.BaseDTO;
import com.shivanshu.secexample.dto.UserDTO;

public interface UserController {
    public ResponseEntity<BaseDTO<List<UserDTO>>> getAllUsers();

    public ResponseEntity<BaseDTO<UserDTO>> getUserById(Long id);

    public ResponseEntity<BaseDTO<UserDTO>> createUser(UserDTO userDTO);

    public ResponseEntity<BaseDTO<UserDTO>> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userDTO);

    public ResponseEntity<BaseDTO<Long>> deleteUser(@PathVariable Long id);

}

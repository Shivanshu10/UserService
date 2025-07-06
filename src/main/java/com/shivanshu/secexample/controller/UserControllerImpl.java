package com.shivanshu.secexample.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivanshu.secexample.dto.BaseDTO;
import com.shivanshu.secexample.dto.UserDTO;
import com.shivanshu.secexample.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserControllerImpl implements UserController {
    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<BaseDTO<List<UserDTO>>> getAllUsers() {
        BaseDTO<List<UserDTO>> users = userService.getAllUsers();
        return ResponseEntity
                .status(users.getStatusCode())
                .body(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<BaseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        BaseDTO<UserDTO> user = userService.getUserById(id);
        return ResponseEntity
                .status(user.getStatusCode())
                .body(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<BaseDTO<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        BaseDTO<UserDTO> user = userService.createUser(userDTO);
        return ResponseEntity
                .status(user.getStatusCode())
                .body(user);
    }

    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.getUserById(#id).getData().getUsername() == authentication.principal.username")
    @PatchMapping("/users/{id}")
    public ResponseEntity<BaseDTO<UserDTO>> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userDTO) {
        BaseDTO<UserDTO> user = userService.updateUser(id, userDTO);
        return ResponseEntity
                .status(user.getStatusCode())
                .body(user);
    }

    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.getUserById(#id).data.username == authentication.principal.username")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<BaseDTO<Long>> deleteUser(@PathVariable Long id) {
        BaseDTO<Long> userId = userService.deleteUser(id);
        return ResponseEntity
                .status(userId.getStatusCode())
                .body(userId);
    }
}

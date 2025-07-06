package com.shivanshu.secexample.service;

import java.util.List;
import java.util.Map;

import com.shivanshu.secexample.dto.BaseDTO;
import com.shivanshu.secexample.dto.UserDTO;

public interface UserService {
    public BaseDTO<List<UserDTO>> getAllUsers();

    public BaseDTO<UserDTO> getUserById(Long id);
    
    public BaseDTO<UserDTO> createUser(UserDTO userDTO);
    
    public BaseDTO<UserDTO> updateUser(Long id, Map<String, Object> updates);
    
    public BaseDTO<Long> deleteUser(Long id);
}

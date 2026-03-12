package com.angola_argentina_portal.mapper;


import org.springframework.stereotype.Component;

import com.angola_argentina_portal.dto.UserDTO;
import com.angola_argentina_portal.model.User;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class UserMapper {

    // Converte User para UserResponseDTO
    public UserDTO.UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO.UserResponseDTO dto = new UserDTO.UserResponseDTO();
        dto.setPkUser(user.getPkUser());
        dto.setFkPerson(user.getFkPerson());
        dto.setFkUserType(user.getFkUserType());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setDeviceToken(user.getDeviceToken());
        dto.setUserCreationDate(user.getUserCreationDate());
        dto.setUserModificationDate(user.getUserModificationDate());

        return dto;
    }

    // Converte CreateUserDTO para User
    public User toEntity(UserDTO.CreateUserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFkPerson(dto.getFkPerson());
        user.setFkUserType(dto.getFkUserType());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // Será hash posteriormente
        user.setActive(dto.isActive());
        user.setDeviceToken(dto.getDeviceToken());
        user.setUserCreationDate(LocalDateTime.now());
        user.setUserModificationDate(LocalDateTime.now());
        user.setFkCustomer(dto.getFkCustomer());

        return user;
    }

    // Atualiza User a partir de UpdateUserDTO
    public void updateFromDTO(User user, UserDTO.UpdateUserDTO dto) {
        if (dto == null || user == null) {
            return;
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getFkUserType() != null) {
            user.setFkUserType(dto.getFkUserType());
        }

        user.setActive(dto.isActive());

        if (dto.getDeviceToken() != null) {
            user.setDeviceToken(dto.getDeviceToken());
        }

        if(dto.getSalt() != null){
            user.setSalt(dto.getSalt());
        }

        if(dto.getPassword() != null){
            user.setPassword(dto.getPassword());
        }

        user.setUserModificationDate(LocalDateTime.now());
    }
}
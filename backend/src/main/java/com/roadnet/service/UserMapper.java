package com.roadnet.service;

import com.roadnet.dto.UserResponse;
import com.roadnet.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        response.setDob(user.getDob() == null ? null : user.getDob().toString());
        response.setGender(user.getGender() == null ? null : user.getGender().name());
        response.setCountry(user.getCountry());
        response.setCity(user.getCity());
        response.setLanguages(user.getLanguages());
        response.setMaritalStatus(user.getMaritalStatus() == null ? null : user.getMaritalStatus().name());
        response.setProfession(user.getProfession());
        response.setBio(user.getBio());
        response.setVerified(user.isVerified());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

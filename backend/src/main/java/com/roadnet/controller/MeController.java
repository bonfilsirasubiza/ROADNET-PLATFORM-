package com.roadnet.controller;

import com.roadnet.dto.UserResponse;
import com.roadnet.security.CurrentUser;
import com.roadnet.service.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MeController {

    private final UserMapper userMapper;

    public MeController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public UserResponse me() {
        return userMapper.toResponse(CurrentUser.get());
    }
}

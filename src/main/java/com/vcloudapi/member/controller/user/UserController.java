package com.vcloudapi.member.controller.user;

import com.vcloudapi.member.service.UserService;
import com.vcloudapi.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ApiResponse<String> getUser() {

        return ApiResponse.success("user", null);
    }
}

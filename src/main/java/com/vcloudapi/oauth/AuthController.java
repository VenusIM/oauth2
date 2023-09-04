package com.vcloudapi.oauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class AuthController {

    @GetMapping("/test")
    public String test(String token) {
        return String.format("Authenticate Success \n Access token : %s", token);
    }
}

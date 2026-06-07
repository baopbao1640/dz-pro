package com.example.platform.admin.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    @GetMapping("/user/info")
    public java.util.Map<String, Object> currentUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED, "未登录"
            );
        }
        String username = principal.getName();
        return java.util.Map.of(
            "name", username,
            "preferred_username", username,
            "authenticated", true
        );
    }
}

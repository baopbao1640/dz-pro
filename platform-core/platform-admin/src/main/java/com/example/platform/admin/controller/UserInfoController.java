package com.example.platform.admin.controller;

import com.platform.core.common.api.ApiResult;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserInfoController {

  /**
   * Returns the identity of the authenticated user for the frontend shell.
   *
   * @param principal the authenticated security principal
   * @return a minimal user profile used by the Phase-1 UI
   */
  @GetMapping("/user/info")
  public ApiResult<java.util.Map<String, Object>> currentUser(Principal principal) {
    if (principal == null || principal.getName() == null) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.UNAUTHORIZED, "未登录");
    }
    String username = principal.getName();
    return ApiResult.success(
        java.util.Map.of(
            "name", username,
            "preferred_username", username,
            "authenticated", true));
  }
}

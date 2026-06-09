package com.platform.core.system.user.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.common.api.PageResult;
import com.platform.core.framework.audit.AuditAction;
import com.platform.core.framework.audit.AuditLog;
import com.platform.core.framework.security.annotation.RequiresPermission;
import com.platform.core.system.user.dto.UserAssignPostDTO;
import com.platform.core.system.user.dto.UserAssignRoleDTO;
import com.platform.core.system.user.dto.UserPageQueryDTO;
import com.platform.core.system.user.dto.UserSaveDTO;
import com.platform.core.system.user.dto.UserStatusDTO;
import com.platform.core.system.user.service.SysUserService;
import com.platform.core.system.user.vo.UserDetailVO;
import com.platform.core.system.user.vo.UserListVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/users")
public class SysUserController {

  private final SysUserService userService;

  public SysUserController(SysUserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @RequiresPermission("system:user:list")
  public ApiResult<PageResult<UserListVO>> page(@Valid UserPageQueryDTO query) {
    return ApiResult.success(userService.page(query));
  }

  @GetMapping("/{id}")
  @RequiresPermission("system:user:query")
  public ApiResult<UserDetailVO> detail(@PathVariable Long id) {
    return ApiResult.success(userService.detail(id));
  }

  @PostMapping
  @RequiresPermission("system:user:add")
  @AuditLog(moduleTitle = "用户管理", action = AuditAction.CREATE)
  public ApiResult<UserDetailVO> create(@Valid @RequestBody UserSaveDTO dto) {
    return ApiResult.success(userService.create(dto));
  }

  @PutMapping("/{id}")
  @RequiresPermission("system:user:edit")
  @AuditLog(moduleTitle = "用户管理", action = AuditAction.UPDATE)
  public ApiResult<UserDetailVO> update(
      @PathVariable Long id, @Valid @RequestBody UserSaveDTO dto) {
    return ApiResult.success(userService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  @RequiresPermission("system:user:status")
  @AuditLog(moduleTitle = "用户管理", action = AuditAction.UPDATE)
  public ApiResult<Void> changeStatus(
      @PathVariable Long id, @Valid @RequestBody UserStatusDTO dto) {
    userService.changeStatus(id, dto);
    return ApiResult.success();
  }

  @PutMapping("/{id}/roles")
  @RequiresPermission("system:user:assign-role")
  @AuditLog(moduleTitle = "用户管理", action = AuditAction.ASSIGN)
  public ApiResult<Void> assignRoles(
      @PathVariable Long id, @Valid @RequestBody UserAssignRoleDTO dto) {
    userService.assignRoles(id, dto);
    return ApiResult.success();
  }

  @PutMapping("/{id}/posts")
  @RequiresPermission("system:user:assign-post")
  @AuditLog(moduleTitle = "用户管理", action = AuditAction.ASSIGN)
  public ApiResult<Void> assignPosts(
      @PathVariable Long id, @Valid @RequestBody UserAssignPostDTO dto) {
    userService.assignPosts(id, dto);
    return ApiResult.success();
  }
}

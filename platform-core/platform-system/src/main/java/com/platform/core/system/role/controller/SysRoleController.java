package com.platform.core.system.role.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.system.role.dto.RoleDataScopeDTO;
import com.platform.core.system.role.dto.RoleMenuAssignDTO;
import com.platform.core.system.role.dto.RolePageQueryDTO;
import com.platform.core.system.role.dto.RoleSaveDTO;
import com.platform.core.system.role.dto.RoleStatusDTO;
import com.platform.core.system.role.service.SysRoleService;
import com.platform.core.system.role.vo.RoleDetailVO;
import com.platform.core.system.role.vo.RoleListVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/roles")
public class SysRoleController {

  private final SysRoleService roleService;

  public SysRoleController(SysRoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public ApiResult<List<RoleListVO>> list(@Valid RolePageQueryDTO query) {
    return ApiResult.success(roleService.list(query));
  }

  @GetMapping("/{id}")
  public ApiResult<RoleDetailVO> detail(@PathVariable Long id) {
    return ApiResult.success(roleService.detail(id));
  }

  @PostMapping
  public ApiResult<RoleDetailVO> create(@Valid @RequestBody RoleSaveDTO dto) {
    return ApiResult.success(roleService.create(dto));
  }

  @PutMapping("/{id}")
  public ApiResult<RoleDetailVO> update(
      @PathVariable Long id, @Valid @RequestBody RoleSaveDTO dto) {
    return ApiResult.success(roleService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  public ApiResult<Void> updateStatus(
      @PathVariable Long id, @Valid @RequestBody RoleStatusDTO dto) {
    roleService.updateStatus(id, dto);
    return ApiResult.success();
  }

  @PutMapping("/{id}/menus")
  public ApiResult<Void> assignMenus(
      @PathVariable Long id, @Valid @RequestBody RoleMenuAssignDTO dto) {
    roleService.assignMenus(id, dto);
    return ApiResult.success();
  }

  @PutMapping("/{id}/data-scope")
  public ApiResult<Void> updateDataScope(
      @PathVariable Long id, @Valid @RequestBody RoleDataScopeDTO dto) {
    roleService.updateDataScope(id, dto);
    return ApiResult.success();
  }
}

package com.platform.core.system.menu.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.framework.audit.AuditAction;
import com.platform.core.framework.audit.AuditLog;
import com.platform.core.framework.security.annotation.RequiresPermission;
import com.platform.core.system.menu.dto.MenuQueryDTO;
import com.platform.core.system.menu.dto.MenuSaveDTO;
import com.platform.core.system.menu.dto.MenuStatusDTO;
import com.platform.core.system.menu.service.SysMenuService;
import com.platform.core.system.menu.vo.MenuTreeVO;
import com.platform.core.system.menu.vo.RouteVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/menus")
public class SysMenuController {

  private final SysMenuService menuService;

  public SysMenuController(SysMenuService menuService) {
    this.menuService = menuService;
  }

  @GetMapping("/tree")
  @RequiresPermission("system:menu:list")
  public ApiResult<List<MenuTreeVO>> tree(@Valid MenuQueryDTO query) {
    return ApiResult.success(menuService.tree(query));
  }

  @GetMapping("/{id}")
  @RequiresPermission("system:menu:query")
  public ApiResult<MenuTreeVO> detail(@PathVariable Long id) {
    return ApiResult.success(menuService.detail(id));
  }

  @PostMapping
  @RequiresPermission("system:menu:add")
  @AuditLog(moduleTitle = "菜单管理", action = AuditAction.CREATE)
  public ApiResult<MenuTreeVO> create(@Valid @RequestBody MenuSaveDTO dto) {
    return ApiResult.success(menuService.create(dto));
  }

  @PutMapping("/{id}")
  @RequiresPermission("system:menu:edit")
  @AuditLog(moduleTitle = "菜单管理", action = AuditAction.UPDATE)
  public ApiResult<MenuTreeVO> update(@PathVariable Long id, @Valid @RequestBody MenuSaveDTO dto) {
    return ApiResult.success(menuService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  @RequiresPermission("system:menu:status")
  @AuditLog(moduleTitle = "菜单管理", action = AuditAction.UPDATE)
  public ApiResult<Void> updateStatus(
      @PathVariable Long id, @Valid @RequestBody MenuStatusDTO dto) {
    menuService.updateStatus(id, dto);
    return ApiResult.success();
  }

  @DeleteMapping("/{id}")
  @RequiresPermission("system:menu:remove")
  @AuditLog(moduleTitle = "菜单管理", action = AuditAction.DELETE)
  public ApiResult<Void> delete(@PathVariable Long id) {
    menuService.delete(id);
    return ApiResult.success();
  }

  @GetMapping("/routes")
  @RequiresPermission("system:menu:list")
  public ApiResult<List<RouteVO>> routes() {
    return ApiResult.success(menuService.routesForRoleIds(List.of()));
  }

  @GetMapping("/permissions")
  @RequiresPermission("system:menu:list")
  public ApiResult<List<String>> permissions() {
    return ApiResult.success(menuService.permissionCodesForRoleIds(List.of()));
  }
}

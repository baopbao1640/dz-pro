package com.platform.core.system.menu.controller;

import com.platform.core.common.api.ApiResult;
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
  public ApiResult<List<MenuTreeVO>> tree(@Valid MenuQueryDTO query) {
    return ApiResult.success(menuService.tree(query));
  }

  @GetMapping("/{id}")
  public ApiResult<MenuTreeVO> detail(@PathVariable Long id) {
    return ApiResult.success(menuService.detail(id));
  }

  @PostMapping
  public ApiResult<MenuTreeVO> create(@Valid @RequestBody MenuSaveDTO dto) {
    return ApiResult.success(menuService.create(dto));
  }

  @PutMapping("/{id}")
  public ApiResult<MenuTreeVO> update(@PathVariable Long id, @Valid @RequestBody MenuSaveDTO dto) {
    return ApiResult.success(menuService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  public ApiResult<Void> updateStatus(
      @PathVariable Long id, @Valid @RequestBody MenuStatusDTO dto) {
    menuService.updateStatus(id, dto);
    return ApiResult.success();
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Long id) {
    menuService.delete(id);
    return ApiResult.success();
  }

  @GetMapping("/routes")
  public ApiResult<List<RouteVO>> routes() {
    return ApiResult.success(menuService.routesForRoleIds(List.of()));
  }

  @GetMapping("/permissions")
  public ApiResult<List<String>> permissions() {
    return ApiResult.success(menuService.permissionCodesForRoleIds(List.of()));
  }
}

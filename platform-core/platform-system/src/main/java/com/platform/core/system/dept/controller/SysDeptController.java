package com.platform.core.system.dept.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.framework.audit.AuditAction;
import com.platform.core.framework.audit.AuditLog;
import com.platform.core.framework.security.annotation.RequiresPermission;
import com.platform.core.system.dept.dto.DeptQueryDTO;
import com.platform.core.system.dept.dto.DeptSaveDTO;
import com.platform.core.system.dept.dto.DeptSortDTO;
import com.platform.core.system.dept.dto.DeptStatusDTO;
import com.platform.core.system.dept.service.SysDeptService;
import com.platform.core.system.dept.vo.DeptTreeVO;
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
@RequestMapping("/api/system/depts")
public class SysDeptController {

  private final SysDeptService deptService;

  public SysDeptController(SysDeptService deptService) {
    this.deptService = deptService;
  }

  @GetMapping("/tree")
  @RequiresPermission("system:dept:list")
  public ApiResult<List<DeptTreeVO>> tree(@Valid DeptQueryDTO query) {
    return ApiResult.success(deptService.tree(query));
  }

  @GetMapping("/{id}")
  @RequiresPermission("system:dept:query")
  public ApiResult<DeptTreeVO> detail(@PathVariable Long id) {
    return ApiResult.success(deptService.detail(id));
  }

  @PostMapping
  @RequiresPermission("system:dept:add")
  @AuditLog(moduleTitle = "部门管理", action = AuditAction.CREATE)
  public ApiResult<DeptTreeVO> create(@Valid @RequestBody DeptSaveDTO dto) {
    return ApiResult.success(deptService.create(dto));
  }

  @PutMapping("/{id}")
  @RequiresPermission("system:dept:edit")
  @AuditLog(moduleTitle = "部门管理", action = AuditAction.UPDATE)
  public ApiResult<DeptTreeVO> update(@PathVariable Long id, @Valid @RequestBody DeptSaveDTO dto) {
    return ApiResult.success(deptService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  @RequiresPermission("system:dept:status")
  @AuditLog(moduleTitle = "部门管理", action = AuditAction.UPDATE)
  public ApiResult<Void> changeStatus(
      @PathVariable Long id, @Valid @RequestBody DeptStatusDTO dto) {
    deptService.changeStatus(id, dto);
    return ApiResult.success();
  }

  @PutMapping("/sort")
  @RequiresPermission("system:dept:edit")
  @AuditLog(moduleTitle = "部门管理", action = AuditAction.UPDATE)
  public ApiResult<Void> sort(@Valid @RequestBody DeptSortDTO dto) {
    deptService.sort(dto);
    return ApiResult.success();
  }

  @DeleteMapping("/{id}")
  @RequiresPermission("system:dept:remove")
  @AuditLog(moduleTitle = "部门管理", action = AuditAction.DELETE)
  public ApiResult<Void> delete(@PathVariable Long id) {
    deptService.delete(id);
    return ApiResult.success();
  }
}

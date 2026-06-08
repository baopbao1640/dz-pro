package com.platform.core.system.dept.controller;

import com.platform.core.common.api.ApiResult;
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
  public ApiResult<List<DeptTreeVO>> tree(@Valid DeptQueryDTO query) {
    return ApiResult.success(deptService.tree(query));
  }

  @GetMapping("/{id}")
  public ApiResult<DeptTreeVO> detail(@PathVariable Long id) {
    return ApiResult.success(deptService.detail(id));
  }

  @PostMapping
  public ApiResult<DeptTreeVO> create(@Valid @RequestBody DeptSaveDTO dto) {
    return ApiResult.success(deptService.create(dto));
  }

  @PutMapping("/{id}")
  public ApiResult<DeptTreeVO> update(@PathVariable Long id, @Valid @RequestBody DeptSaveDTO dto) {
    return ApiResult.success(deptService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  public ApiResult<Void> changeStatus(
      @PathVariable Long id, @Valid @RequestBody DeptStatusDTO dto) {
    deptService.changeStatus(id, dto);
    return ApiResult.success();
  }

  @PutMapping("/sort")
  public ApiResult<Void> sort(@Valid @RequestBody DeptSortDTO dto) {
    deptService.sort(dto);
    return ApiResult.success();
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Long id) {
    deptService.delete(id);
    return ApiResult.success();
  }
}

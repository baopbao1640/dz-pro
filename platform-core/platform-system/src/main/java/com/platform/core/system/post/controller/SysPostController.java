package com.platform.core.system.post.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.common.api.PageResult;
import com.platform.core.framework.audit.AuditAction;
import com.platform.core.framework.audit.AuditLog;
import com.platform.core.framework.security.annotation.RequiresPermission;
import com.platform.core.system.post.dto.PostPageQueryDTO;
import com.platform.core.system.post.dto.PostSaveDTO;
import com.platform.core.system.post.dto.PostStatusDTO;
import com.platform.core.system.post.service.SysPostService;
import com.platform.core.system.post.vo.PostListVO;
import jakarta.validation.Valid;
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
@RequestMapping("/api/system/posts")
public class SysPostController {

  private final SysPostService postService;

  public SysPostController(SysPostService postService) {
    this.postService = postService;
  }

  @GetMapping
  @RequiresPermission("system:post:list")
  public ApiResult<PageResult<PostListVO>> page(@Valid PostPageQueryDTO query) {
    return ApiResult.success(postService.page(query));
  }

  @GetMapping("/{id}")
  @RequiresPermission("system:post:query")
  public ApiResult<PostListVO> detail(@PathVariable Long id) {
    return ApiResult.success(postService.detail(id));
  }

  @PostMapping
  @RequiresPermission("system:post:add")
  @AuditLog(moduleTitle = "岗位管理", action = AuditAction.CREATE)
  public ApiResult<PostListVO> create(@Valid @RequestBody PostSaveDTO dto) {
    return ApiResult.success(postService.create(dto));
  }

  @PutMapping("/{id}")
  @RequiresPermission("system:post:edit")
  @AuditLog(moduleTitle = "岗位管理", action = AuditAction.UPDATE)
  public ApiResult<PostListVO> update(@PathVariable Long id, @Valid @RequestBody PostSaveDTO dto) {
    return ApiResult.success(postService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  @RequiresPermission("system:post:status")
  @AuditLog(moduleTitle = "岗位管理", action = AuditAction.UPDATE)
  public ApiResult<Void> changeStatus(
      @PathVariable Long id, @Valid @RequestBody PostStatusDTO dto) {
    postService.changeStatus(id, dto);
    return ApiResult.success();
  }

  @DeleteMapping("/{id}")
  @RequiresPermission("system:post:remove")
  @AuditLog(moduleTitle = "岗位管理", action = AuditAction.DELETE)
  public ApiResult<Void> delete(@PathVariable Long id) {
    postService.delete(id);
    return ApiResult.success();
  }
}

package com.platform.core.system.post.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.common.api.PageResult;
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
  public ApiResult<PageResult<PostListVO>> page(@Valid PostPageQueryDTO query) {
    return ApiResult.success(postService.page(query));
  }

  @GetMapping("/{id}")
  public ApiResult<PostListVO> detail(@PathVariable Long id) {
    return ApiResult.success(postService.detail(id));
  }

  @PostMapping
  public ApiResult<PostListVO> create(@Valid @RequestBody PostSaveDTO dto) {
    return ApiResult.success(postService.create(dto));
  }

  @PutMapping("/{id}")
  public ApiResult<PostListVO> update(@PathVariable Long id, @Valid @RequestBody PostSaveDTO dto) {
    return ApiResult.success(postService.update(id, dto));
  }

  @PatchMapping("/{id}/status")
  public ApiResult<Void> changeStatus(
      @PathVariable Long id, @Valid @RequestBody PostStatusDTO dto) {
    postService.changeStatus(id, dto);
    return ApiResult.success();
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Long id) {
    postService.delete(id);
    return ApiResult.success();
  }
}

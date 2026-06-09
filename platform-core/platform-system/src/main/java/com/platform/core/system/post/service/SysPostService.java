package com.platform.core.system.post.service;

import com.platform.core.common.api.PageResult;
import com.platform.core.system.post.dto.PostPageQueryDTO;
import com.platform.core.system.post.dto.PostSaveDTO;
import com.platform.core.system.post.dto.PostStatusDTO;
import com.platform.core.system.post.vo.PostListVO;

/**
 * 岗位管理服务契约，定义岗位基础资料维护能力。
 *
 * <p>职责：维护岗位编码、名称、排序、状态和删除约束。
 *
 * <p>边界：不直接分配用户岗位关系，用户岗位关系由用户服务维护。
 *
 * <p>当前阶段能力：支持分页、详情、创建、更新、状态切换和删除。
 */
public interface SysPostService {

  PageResult<PostListVO> page(PostPageQueryDTO query);

  PostListVO detail(Long id);

  PostListVO create(PostSaveDTO dto);

  PostListVO update(Long id, PostSaveDTO dto);

  void changeStatus(Long id, PostStatusDTO dto);

  void delete(Long id);
}

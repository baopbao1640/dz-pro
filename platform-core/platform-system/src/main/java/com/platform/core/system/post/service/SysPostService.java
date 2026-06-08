package com.platform.core.system.post.service;

import com.platform.core.common.api.PageResult;
import com.platform.core.system.post.dto.PostPageQueryDTO;
import com.platform.core.system.post.dto.PostSaveDTO;
import com.platform.core.system.post.dto.PostStatusDTO;
import com.platform.core.system.post.vo.PostListVO;

public interface SysPostService {

  PageResult<PostListVO> page(PostPageQueryDTO query);

  PostListVO detail(Long id);

  PostListVO create(PostSaveDTO dto);

  PostListVO update(Long id, PostSaveDTO dto);

  void changeStatus(Long id, PostStatusDTO dto);

  void delete(Long id);
}

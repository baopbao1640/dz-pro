package com.platform.core.system.post.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.post.domain.SysPost;
import com.platform.core.system.post.dto.PostSaveDTO;
import com.platform.core.system.post.mapper.SysPostMapper;
import org.junit.jupiter.api.Test;

class SysPostServiceImplTest {

  @Test
  void createPostRejectsDuplicateActiveCode() {
    SysPostMapper mapper = org.mockito.Mockito.mock(SysPostMapper.class);
    when(mapper.countActiveByPostCode("DEV", null)).thenReturn(1L);
    SysPostServiceImpl service = new SysPostServiceImpl(mapper);
    PostSaveDTO dto = new PostSaveDTO();
    dto.setPostCode("DEV");
    dto.setPostName("Developer");
    dto.setPostSort(1);
    dto.setStatus(StatusConstants.NORMAL);

    assertThatThrownBy(() -> service.create(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Post code already exists");
  }

  @Test
  void deletePostRejectsActiveUserBindings() {
    SysPostMapper mapper = org.mockito.Mockito.mock(SysPostMapper.class);
    SysPost post = new SysPost();
    post.setId(1L);
    when(mapper.selectActiveById(1L)).thenReturn(post);
    when(mapper.countActiveUsersByPostId(1L)).thenReturn(1L);
    SysPostServiceImpl service = new SysPostServiceImpl(mapper);

    assertThatThrownBy(() -> service.delete(1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("active users");

    verify(mapper).countActiveUsersByPostId(1L);
  }
}

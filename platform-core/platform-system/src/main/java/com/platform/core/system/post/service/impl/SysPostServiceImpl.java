package com.platform.core.system.post.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.core.common.api.PageResult;
import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.post.domain.SysPost;
import com.platform.core.system.post.dto.PostPageQueryDTO;
import com.platform.core.system.post.dto.PostSaveDTO;
import com.platform.core.system.post.dto.PostStatusDTO;
import com.platform.core.system.post.mapper.SysPostMapper;
import com.platform.core.system.post.service.SysPostService;
import com.platform.core.system.post.vo.PostListVO;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 岗位管理服务实现，负责岗位唯一性、状态和占用关系校验。
 *
 * <p>职责：维护岗位基础数据，并在删除前确认未被用户占用。
 *
 * <p>边界：不处理用户岗位分配事务，避免与用户服务职责重叠。
 *
 * <p>当前阶段能力：支持岗位分页 total 修复和审计入口调用。
 */
@Service
public class SysPostServiceImpl implements SysPostService {

  private final SysPostMapper postMapper;

  public SysPostServiceImpl(SysPostMapper postMapper) {
    this.postMapper = postMapper;
  }

  @Override
  public PageResult<PostListVO> page(PostPageQueryDTO query) {
    Page<PostListVO> page =
        postMapper.selectPostPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    return PageResult.of(
        page.getRecords(), postMapper.countPostPage(query), page.getCurrent(), page.getSize());
  }

  @Override
  public PostListVO detail(Long id) {
    PostListVO detail = postMapper.selectPostDetail(id);
    if (detail == null) {
      throw new IllegalArgumentException("Post does not exist");
    }
    return detail;
  }

  @Override
  @Transactional
  public PostListVO create(PostSaveDTO dto) {
    ensureUniquePostCode(dto.getPostCode(), null);
    SysPost post = new SysPost();
    applySave(post, dto);
    post.setDelFlag(StatusConstants.NOT_DELETED);
    post.setCreateTime(OffsetDateTime.now());
    postMapper.insert(post);
    return toListVO(post, 0L);
  }

  @Override
  @Transactional
  public PostListVO update(Long id, PostSaveDTO dto) {
    requireActive(id);
    ensureUniquePostCode(dto.getPostCode(), id);
    SysPost post = new SysPost();
    post.setId(id);
    applySave(post, dto);
    post.setUpdateTime(OffsetDateTime.now());
    postMapper.updateById(post);
    return detail(id);
  }

  @Override
  @Transactional
  public void changeStatus(Long id, PostStatusDTO dto) {
    requireActive(id);
    if (StatusConstants.DISABLED.equals(dto.getStatus())
        && postMapper.countActiveUsersByPostId(id) > 0) {
      throw new IllegalStateException("Post has active users");
    }
    SysPost post = new SysPost();
    post.setId(id);
    post.setStatus(dto.getStatus());
    post.setUpdateTime(OffsetDateTime.now());
    postMapper.updateById(post);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    requireActive(id);
    if (postMapper.countActiveUsersByPostId(id) > 0) {
      throw new IllegalStateException("Post has active users");
    }
    SysPost post = new SysPost();
    post.setId(id);
    post.setDelFlag(StatusConstants.DELETED);
    post.setUpdateTime(OffsetDateTime.now());
    postMapper.updateById(post);
  }

  private SysPost requireActive(Long id) {
    SysPost post = postMapper.selectActiveById(id);
    if (post == null) {
      throw new IllegalArgumentException("Post does not exist");
    }
    return post;
  }

  private void ensureUniquePostCode(String postCode, Long excludeId) {
    if (postMapper.countActiveByPostCode(postCode, excludeId) > 0) {
      throw new IllegalArgumentException("Post code already exists");
    }
  }

  private void applySave(SysPost post, PostSaveDTO dto) {
    post.setPostCode(dto.getPostCode());
    post.setPostName(dto.getPostName());
    post.setPostSort(dto.getPostSort());
    post.setStatus(dto.getStatus() == null ? StatusConstants.NORMAL : dto.getStatus());
    post.setRemark(dto.getRemark());
  }

  private PostListVO toListVO(SysPost post, long boundUserCount) {
    PostListVO vo = new PostListVO();
    vo.setId(post.getId());
    vo.setPostCode(post.getPostCode());
    vo.setPostName(post.getPostName());
    vo.setPostSort(post.getPostSort());
    vo.setStatus(post.getStatus());
    vo.setBoundUserCount(boundUserCount);
    vo.setCreateTime(post.getCreateTime());
    return vo;
  }
}

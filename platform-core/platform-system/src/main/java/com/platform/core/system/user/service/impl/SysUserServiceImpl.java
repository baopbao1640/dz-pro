package com.platform.core.system.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.core.common.api.PageResult;
import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.user.domain.SysUser;
import com.platform.core.system.user.dto.UserAssignPostDTO;
import com.platform.core.system.user.dto.UserAssignRoleDTO;
import com.platform.core.system.user.dto.UserPageQueryDTO;
import com.platform.core.system.user.dto.UserRelationRow;
import com.platform.core.system.user.dto.UserSaveDTO;
import com.platform.core.system.user.dto.UserStatusDTO;
import com.platform.core.system.user.mapper.SysUserMapper;
import com.platform.core.system.user.service.SysUserService;
import com.platform.core.system.user.vo.UserDetailVO;
import com.platform.core.system.user.vo.UserListVO;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl implements SysUserService {

  private static final String DEFAULT_USER_TYPE = "00";
  private static final String DEFAULT_SEX = "2";

  private final SysUserMapper userMapper;

  public SysUserServiceImpl(SysUserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public PageResult<UserListVO> page(UserPageQueryDTO query) {
    Page<UserListVO> page =
        userMapper.selectUserPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    return PageResult.of(
        page.getRecords(), userMapper.countUserPage(query), page.getCurrent(), page.getSize());
  }

  @Override
  public UserDetailVO detail(Long id) {
    UserDetailVO detail = userMapper.selectUserDetail(id);
    if (detail == null) {
      throw new IllegalArgumentException("User does not exist");
    }
    detail.setRoleIds(userMapper.selectRoleIdsByUserId(id));
    detail.setPostIds(userMapper.selectPostIdsByUserId(id));
    return detail;
  }

  @Override
  @Transactional
  public UserDetailVO create(UserSaveDTO dto) {
    validateSave(dto, null);
    SysUser user = new SysUser();
    fillUser(user, dto);
    user.setUserType(DEFAULT_USER_TYPE);
    user.setDelFlag(StatusConstants.NOT_DELETED);
    user.setCreateTime(OffsetDateTime.now());
    userMapper.insert(user);
    replaceRoles(user.getId(), dto.getRoleIds());
    replacePosts(user.getId(), dto.getPostIds());
    return detail(user.getId());
  }

  @Override
  @Transactional
  public UserDetailVO update(Long id, UserSaveDTO dto) {
    requireUser(id);
    validateSave(dto, id);
    SysUser user = new SysUser();
    user.setId(id);
    fillUser(user, dto);
    user.setUpdateTime(OffsetDateTime.now());
    userMapper.updateById(user);
    replaceRoles(id, dto.getRoleIds());
    replacePosts(id, dto.getPostIds());
    return detail(id);
  }

  @Override
  @Transactional
  public void changeStatus(Long id, UserStatusDTO dto) {
    requireUser(id);
    if (!StatusConstants.NORMAL.equals(dto.getStatus())
        && !StatusConstants.DISABLED.equals(dto.getStatus())) {
      throw new IllegalArgumentException("Unsupported user status");
    }
    SysUser user = new SysUser();
    user.setId(id);
    user.setStatus(dto.getStatus());
    user.setUpdateTime(OffsetDateTime.now());
    userMapper.updateById(user);
  }

  @Override
  @Transactional
  public void assignRoles(Long id, UserAssignRoleDTO dto) {
    requireUser(id);
    replaceRoles(id, dto.getRoleIds());
  }

  @Override
  @Transactional
  public void assignPosts(Long id, UserAssignPostDTO dto) {
    requireUser(id);
    replacePosts(id, dto.getPostIds());
  }

  private SysUser requireUser(Long id) {
    SysUser user = userMapper.selectById(id);
    if (user == null || StatusConstants.DELETED.equals(user.getDelFlag())) {
      throw new IllegalArgumentException("User does not exist");
    }
    return user;
  }

  private void validateSave(UserSaveDTO dto, Long excludeId) {
    if (!StringUtils.hasText(dto.getKeycloakUserId())) {
      throw new IllegalArgumentException("Keycloak user ID is required");
    }
    if (!StringUtils.hasText(dto.getUserName())) {
      throw new IllegalArgumentException("Username is required");
    }
    if (!StringUtils.hasText(dto.getNickName())) {
      throw new IllegalArgumentException("Display name is required");
    }
    if (userMapper.countActiveByKeycloakUserId(dto.getKeycloakUserId(), excludeId) > 0) {
      throw new IllegalArgumentException("Keycloak user ID already exists");
    }
    if (StringUtils.hasText(dto.getStatus())
        && !StatusConstants.NORMAL.equals(dto.getStatus())
        && !StatusConstants.DISABLED.equals(dto.getStatus())) {
      throw new IllegalArgumentException("Unsupported user status");
    }
  }

  private void fillUser(SysUser user, UserSaveDTO dto) {
    user.setKeycloakUserId(dto.getKeycloakUserId());
    user.setDeptId(dto.getDeptId());
    user.setUserName(dto.getUserName());
    user.setNickName(dto.getNickName());
    user.setEmail(dto.getEmail());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setSex(StringUtils.hasText(dto.getSex()) ? dto.getSex() : DEFAULT_SEX);
    user.setStatus(StringUtils.hasText(dto.getStatus()) ? dto.getStatus() : StatusConstants.NORMAL);
    user.setRemark(dto.getRemark());
  }

  private void replaceRoles(Long id, java.util.List<Long> roleIds) {
    userMapper.deleteRolesByUserId(id);
    if (roleIds != null && !roleIds.isEmpty()) {
      userMapper.insertUserRoles(id, toRelationRows(roleIds));
    }
  }

  private void replacePosts(Long id, java.util.List<Long> postIds) {
    userMapper.deletePostsByUserId(id);
    if (postIds != null && !postIds.isEmpty()) {
      userMapper.insertUserPosts(id, toRelationRows(postIds));
    }
  }

  private java.util.List<UserRelationRow> toRelationRows(java.util.List<Long> relationIds) {
    return relationIds.stream()
        .map(relationId -> new UserRelationRow(IdWorker.getId(), relationId))
        .toList();
  }
}

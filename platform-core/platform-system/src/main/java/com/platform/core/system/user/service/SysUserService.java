package com.platform.core.system.user.service;

import com.platform.core.common.api.PageResult;
import com.platform.core.system.user.dto.UserAssignPostDTO;
import com.platform.core.system.user.dto.UserAssignRoleDTO;
import com.platform.core.system.user.dto.UserPageQueryDTO;
import com.platform.core.system.user.dto.UserSaveDTO;
import com.platform.core.system.user.dto.UserStatusDTO;
import com.platform.core.system.user.vo.UserDetailVO;
import com.platform.core.system.user.vo.UserListVO;

/**
 * 用户管理服务契约，定义本地业务用户和授权关系的核心维护能力。
 *
 * <p>职责：维护 `sys_user` 以及用户-角色、用户-岗位关系。
 *
 * <p>边界：不创建 Keycloak 用户，不保存密码，不签发 token。
 *
 * <p>当前阶段能力：支持分页、详情、创建、更新、状态切换和关系分配。
 */
public interface SysUserService {

  PageResult<UserListVO> page(UserPageQueryDTO query);

  UserDetailVO detail(Long id);

  UserDetailVO create(UserSaveDTO dto);

  UserDetailVO update(Long id, UserSaveDTO dto);

  void changeStatus(Long id, UserStatusDTO dto);

  void assignRoles(Long id, UserAssignRoleDTO dto);

  void assignPosts(Long id, UserAssignPostDTO dto);
}

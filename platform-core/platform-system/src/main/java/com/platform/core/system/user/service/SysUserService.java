package com.platform.core.system.user.service;

import com.platform.core.common.api.PageResult;
import com.platform.core.system.user.dto.UserAssignPostDTO;
import com.platform.core.system.user.dto.UserAssignRoleDTO;
import com.platform.core.system.user.dto.UserPageQueryDTO;
import com.platform.core.system.user.dto.UserSaveDTO;
import com.platform.core.system.user.dto.UserStatusDTO;
import com.platform.core.system.user.vo.UserDetailVO;
import com.platform.core.system.user.vo.UserListVO;

public interface SysUserService {

  PageResult<UserListVO> page(UserPageQueryDTO query);

  UserDetailVO detail(Long id);

  UserDetailVO create(UserSaveDTO dto);

  UserDetailVO update(Long id, UserSaveDTO dto);

  void changeStatus(Long id, UserStatusDTO dto);

  void assignRoles(Long id, UserAssignRoleDTO dto);

  void assignPosts(Long id, UserAssignPostDTO dto);
}

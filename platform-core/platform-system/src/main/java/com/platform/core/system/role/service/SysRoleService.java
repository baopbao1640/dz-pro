package com.platform.core.system.role.service;

import com.platform.core.system.role.dto.RoleDataScopeDTO;
import com.platform.core.system.role.dto.RoleMenuAssignDTO;
import com.platform.core.system.role.dto.RolePageQueryDTO;
import com.platform.core.system.role.dto.RoleSaveDTO;
import com.platform.core.system.role.dto.RoleStatusDTO;
import com.platform.core.system.role.vo.RoleDetailVO;
import com.platform.core.system.role.vo.RoleListVO;
import java.util.List;

/** Role management service contract. */
public interface SysRoleService {

  List<RoleListVO> list(RolePageQueryDTO query);

  RoleDetailVO detail(Long id);

  RoleDetailVO create(RoleSaveDTO dto);

  RoleDetailVO update(Long id, RoleSaveDTO dto);

  void updateStatus(Long id, RoleStatusDTO dto);

  void assignMenus(Long roleId, RoleMenuAssignDTO dto);

  void updateDataScope(Long roleId, RoleDataScopeDTO dto);
}

package com.platform.core.system.role.service;

import com.platform.core.system.role.dto.RoleDataScopeDTO;
import com.platform.core.system.role.dto.RoleMenuAssignDTO;
import com.platform.core.system.role.dto.RolePageQueryDTO;
import com.platform.core.system.role.dto.RoleSaveDTO;
import com.platform.core.system.role.dto.RoleStatusDTO;
import com.platform.core.system.role.vo.RoleDetailVO;
import com.platform.core.system.role.vo.RoleListVO;
import java.util.List;

/**
 * 角色管理服务契约，定义角色基础信息、菜单授权和数据范围配置能力。
 *
 * <p>职责：维护 `sys_role`、角色菜单关系和角色部门数据范围。
 *
 * <p>边界：不解析当前用户权限，不直接生成数据权限 SQL。
 *
 * <p>当前阶段能力：支持角色启停、菜单授权和 data scope 配置落库。
 */
public interface SysRoleService {

  List<RoleListVO> list(RolePageQueryDTO query);

  RoleDetailVO detail(Long id);

  RoleDetailVO create(RoleSaveDTO dto);

  RoleDetailVO update(Long id, RoleSaveDTO dto);

  void updateStatus(Long id, RoleStatusDTO dto);

  void assignMenus(Long roleId, RoleMenuAssignDTO dto);

  void updateDataScope(Long roleId, RoleDataScopeDTO dto);
}

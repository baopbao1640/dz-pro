package com.platform.core.system.role.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.role.domain.SysRole;
import com.platform.core.system.role.domain.SysRoleDept;
import com.platform.core.system.role.domain.SysRoleMenu;
import com.platform.core.system.role.dto.RoleDataScopeDTO;
import com.platform.core.system.role.dto.RoleMenuAssignDTO;
import com.platform.core.system.role.dto.RolePageQueryDTO;
import com.platform.core.system.role.dto.RoleSaveDTO;
import com.platform.core.system.role.dto.RoleStatusDTO;
import com.platform.core.system.role.enums.RoleDataScope;
import com.platform.core.system.role.mapper.SysRoleDeptMapper;
import com.platform.core.system.role.mapper.SysRoleMapper;
import com.platform.core.system.role.mapper.SysRoleMenuMapper;
import com.platform.core.system.role.service.SysRoleService;
import com.platform.core.system.role.vo.RoleDetailVO;
import com.platform.core.system.role.vo.RoleListVO;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** Minimal role service implementation for Phase 3A backend scaffolding. */
@Service
public class SysRoleServiceImpl implements SysRoleService {

  private final SysRoleMapper roleMapper;
  private final SysRoleMenuMapper roleMenuMapper;
  private final SysRoleDeptMapper roleDeptMapper;

  public SysRoleServiceImpl(
      SysRoleMapper roleMapper,
      SysRoleMenuMapper roleMenuMapper,
      SysRoleDeptMapper roleDeptMapper) {
    this.roleMapper = roleMapper;
    this.roleMenuMapper = roleMenuMapper;
    this.roleDeptMapper = roleDeptMapper;
  }

  @Override
  public List<RoleListVO> list(RolePageQueryDTO query) {
    return roleMapper.selectRoleList(query).stream().map(this::toListVO).toList();
  }

  @Override
  public RoleDetailVO detail(Long id) {
    SysRole role = requireRole(id);
    return toDetailVO(role);
  }

  @Override
  @Transactional
  public RoleDetailVO create(RoleSaveDTO dto) {
    validateSave(dto, null);
    SysRole role = new SysRole();
    fillRole(role, dto);
    role.setDelFlag(StatusConstants.NOT_DELETED);
    role.setCreateTime(OffsetDateTime.now());
    roleMapper.insert(role);
    return toDetailVO(role);
  }

  @Override
  @Transactional
  public RoleDetailVO update(Long id, RoleSaveDTO dto) {
    requireRole(id);
    validateSave(dto, id);
    SysRole role = new SysRole();
    role.setId(id);
    fillRole(role, dto);
    role.setUpdateTime(OffsetDateTime.now());
    roleMapper.updateById(role);
    return detail(id);
  }

  @Override
  @Transactional
  public void updateStatus(Long id, RoleStatusDTO dto) {
    requireRole(id);
    if (!StatusConstants.NORMAL.equals(dto.getStatus())
        && !StatusConstants.DISABLED.equals(dto.getStatus())) {
      throw new IllegalArgumentException("Unsupported role status");
    }
    SysRole role = new SysRole();
    role.setId(id);
    role.setStatus(dto.getStatus());
    role.setUpdateTime(OffsetDateTime.now());
    roleMapper.updateById(role);
  }

  @Override
  @Transactional
  public void assignMenus(Long roleId, RoleMenuAssignDTO dto) {
    requireRole(roleId);
    validateYesNo(dto.getMenuCheckStrictly(), "menuCheckStrictly");
    SysRole role = new SysRole();
    role.setId(roleId);
    role.setMenuCheckStrictly(dto.getMenuCheckStrictly());
    role.setUpdateTime(OffsetDateTime.now());
    roleMapper.updateById(role);
    roleMenuMapper.deleteByRoleId(roleId);
    List<SysRoleMenu> roleMenus = toRoleMenus(roleId, dto.getMenuIds());
    if (!roleMenus.isEmpty()) {
      roleMenuMapper.batchInsert(roleMenus);
    }
  }

  @Override
  @Transactional
  public void updateDataScope(Long roleId, RoleDataScopeDTO dto) {
    requireRole(roleId);
    validateDataScope(dto);
    SysRole role = new SysRole();
    role.setId(roleId);
    role.setDataScope(dto.getDataScope());
    role.setDeptCheckStrictly(dto.getDeptCheckStrictly());
    role.setUpdateTime(OffsetDateTime.now());
    roleMapper.updateById(role);
    roleDeptMapper.deleteByRoleId(roleId);
    if (RoleDataScope.CUSTOM.getCode().equals(dto.getDataScope())) {
      List<SysRoleDept> roleDepts = toRoleDepts(roleId, dto.getDeptIds());
      if (!roleDepts.isEmpty()) {
        roleDeptMapper.batchInsert(roleDepts);
      }
    }
  }

  private void validateSave(RoleSaveDTO dto, Long excludeId) {
    if (!StringUtils.hasText(dto.getRoleName())) {
      throw new IllegalArgumentException("Role name is required");
    }
    if (!StringUtils.hasText(dto.getRoleKey())) {
      throw new IllegalArgumentException("Role key is required");
    }
    if (roleMapper.countActiveByRoleKey(dto.getRoleKey(), excludeId) > 0) {
      throw new IllegalArgumentException("Role key already exists");
    }
    if (dto.getRoleSort() == null || dto.getRoleSort() < 0) {
      throw new IllegalArgumentException("Role sort must be non-negative");
    }
    if (!RoleDataScope.supports(dto.getDataScope())) {
      throw new IllegalArgumentException("Unsupported role data scope");
    }
    validateYesNo(dto.getMenuCheckStrictly(), "menuCheckStrictly");
    validateYesNo(dto.getDeptCheckStrictly(), "deptCheckStrictly");
  }

  private void validateDataScope(RoleDataScopeDTO dto) {
    if (!RoleDataScope.supports(dto.getDataScope())) {
      throw new IllegalArgumentException("Unsupported role data scope");
    }
    validateYesNo(dto.getDeptCheckStrictly(), "deptCheckStrictly");
    if (RoleDataScope.CUSTOM.getCode().equals(dto.getDataScope())
        && CollectionUtils.isEmpty(dto.getDeptIds())) {
      throw new IllegalArgumentException("Custom data scope requires department IDs");
    }
  }

  private void validateYesNo(String value, String fieldName) {
    if (!StatusConstants.YES.equals(value) && !StatusConstants.NO.equals(value)) {
      throw new IllegalArgumentException(fieldName + " must be Y or N");
    }
  }

  private SysRole requireRole(Long id) {
    SysRole role = roleMapper.selectActiveById(id);
    if (role == null) {
      throw new IllegalArgumentException("Role does not exist");
    }
    return role;
  }

  private void fillRole(SysRole role, RoleSaveDTO dto) {
    role.setRoleName(dto.getRoleName());
    role.setRoleKey(dto.getRoleKey());
    role.setRoleSort(dto.getRoleSort());
    role.setDataScope(dto.getDataScope());
    role.setMenuCheckStrictly(dto.getMenuCheckStrictly());
    role.setDeptCheckStrictly(dto.getDeptCheckStrictly());
    role.setStatus(StringUtils.hasText(dto.getStatus()) ? dto.getStatus() : StatusConstants.NORMAL);
    role.setRemark(dto.getRemark());
  }

  private List<SysRoleMenu> toRoleMenus(Long roleId, Collection<Long> menuIds) {
    if (CollectionUtils.isEmpty(menuIds)) {
      return List.of();
    }
    List<SysRoleMenu> roleMenus = new ArrayList<>();
    for (Long menuId : menuIds) {
      SysRoleMenu roleMenu = new SysRoleMenu();
      roleMenu.setId(IdWorker.getId());
      roleMenu.setRoleId(roleId);
      roleMenu.setMenuId(menuId);
      roleMenu.setCreateTime(OffsetDateTime.now());
      roleMenus.add(roleMenu);
    }
    return roleMenus;
  }

  private List<SysRoleDept> toRoleDepts(Long roleId, Collection<Long> deptIds) {
    if (CollectionUtils.isEmpty(deptIds)) {
      return List.of();
    }
    List<SysRoleDept> roleDepts = new ArrayList<>();
    for (Long deptId : deptIds) {
      SysRoleDept roleDept = new SysRoleDept();
      roleDept.setId(IdWorker.getId());
      roleDept.setRoleId(roleId);
      roleDept.setDeptId(deptId);
      roleDept.setCreateTime(OffsetDateTime.now());
      roleDepts.add(roleDept);
    }
    return roleDepts;
  }

  private RoleListVO toListVO(SysRole role) {
    RoleListVO vo = new RoleListVO();
    vo.setId(role.getId());
    vo.setRoleName(role.getRoleName());
    vo.setRoleKey(role.getRoleKey());
    vo.setRoleSort(role.getRoleSort());
    vo.setDataScope(role.getDataScope());
    vo.setDataScopeLabel(dataScopeLabel(role.getDataScope()));
    vo.setStatus(role.getStatus());
    vo.setCreateTime(role.getCreateTime());
    return vo;
  }

  private RoleDetailVO toDetailVO(SysRole role) {
    RoleDetailVO vo = new RoleDetailVO();
    vo.setId(role.getId());
    vo.setRoleName(role.getRoleName());
    vo.setRoleKey(role.getRoleKey());
    vo.setRoleSort(role.getRoleSort());
    vo.setDataScope(role.getDataScope());
    vo.setMenuCheckStrictly(role.getMenuCheckStrictly());
    vo.setDeptCheckStrictly(role.getDeptCheckStrictly());
    vo.setStatus(role.getStatus());
    vo.setRemark(role.getRemark());
    vo.setCreateTime(role.getCreateTime());
    vo.setMenuIds(roleMenuMapper.selectMenuIdsByRoleId(role.getId()));
    vo.setDeptIds(roleDeptMapper.selectDeptIdsByRoleId(role.getId()));
    vo.setAssignedUserCount(roleMapper.countAssignedUsers(role.getId()));
    return vo;
  }

  private String dataScopeLabel(String dataScope) {
    return switch (dataScope) {
      case "1" -> "All data";
      case "2" -> "Custom departments";
      case "3" -> "Current department";
      case "4" -> "Current department and children";
      case "5" -> "Current user only";
      default -> "";
    };
  }
}

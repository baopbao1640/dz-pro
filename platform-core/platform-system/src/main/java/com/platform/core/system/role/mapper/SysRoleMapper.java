package com.platform.core.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.role.domain.SysRole;
import com.platform.core.system.role.dto.RolePageQueryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色 Mapper，封装 `sys_role` 查询和角色授权相关统计。
 *
 * <p>职责：提供角色列表、详情、唯一性、用户占用和当前用户角色查询。
 *
 * <p>边界：不维护角色菜单关系和角色部门关系的批量写入。
 *
 * <p>当前阶段能力：支撑当前用户权限组装和角色管理 CRUD。
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

  List<SysRole> selectRoleList(@Param("query") RolePageQueryDTO query);

  SysRole selectActiveById(@Param("id") Long id);

  Long countActiveByRoleKey(@Param("roleKey") String roleKey, @Param("excludeId") Long excludeId);

  Long countAssignedUsers(@Param("roleId") Long roleId);

  List<SysRole> selectEnabledRolesByUserId(@Param("userId") Long userId);
}

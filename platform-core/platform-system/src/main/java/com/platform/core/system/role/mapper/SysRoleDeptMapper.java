package com.platform.core.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.role.domain.SysRoleDept;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色部门范围 Mapper，负责 `sys_role_dept` 自定义数据范围关系维护。
 *
 * <p>职责：写入和清理角色可访问部门集合。
 *
 * <p>边界：不解释部门树，不生成 data scope SQL 条件。
 *
 * <p>当前阶段能力：支撑 CUSTOM 数据范围的关系落库，真实 SQL 生效后续补齐。
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

  List<Long> selectDeptIdsByRoleId(@Param("roleId") Long roleId);

  int deleteByRoleId(@Param("roleId") Long roleId);

  int batchInsert(@Param("roleDepts") Collection<SysRoleDept> roleDepts);
}

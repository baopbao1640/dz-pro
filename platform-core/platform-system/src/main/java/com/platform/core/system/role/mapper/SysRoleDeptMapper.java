package com.platform.core.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.role.domain.SysRoleDept;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** Persistence contract for sys_role_dept relationships. */
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

  List<Long> selectDeptIdsByRoleId(@Param("roleId") Long roleId);

  int deleteByRoleId(@Param("roleId") Long roleId);

  int batchInsert(@Param("roleDepts") Collection<SysRoleDept> roleDepts);
}

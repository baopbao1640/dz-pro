package com.platform.core.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.role.domain.SysRole;
import com.platform.core.system.role.dto.RolePageQueryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** Persistence contract for sys_role. */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

  List<SysRole> selectRoleList(@Param("query") RolePageQueryDTO query);

  SysRole selectActiveById(@Param("id") Long id);

  Long countActiveByRoleKey(@Param("roleKey") String roleKey, @Param("excludeId") Long excludeId);

  Long countAssignedUsers(@Param("roleId") Long roleId);
}

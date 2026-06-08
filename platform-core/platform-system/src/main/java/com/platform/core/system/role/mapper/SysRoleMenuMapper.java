package com.platform.core.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.role.domain.SysRoleMenu;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** Persistence contract for sys_role_menu relationships. */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

  List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

  int deleteByRoleId(@Param("roleId") Long roleId);

  int batchInsert(@Param("roleMenus") Collection<SysRoleMenu> roleMenus);
}

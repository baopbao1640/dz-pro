package com.platform.core.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.role.domain.SysRoleMenu;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色菜单 Mapper，负责 `sys_role_menu` 授权关系维护。
 *
 * <p>职责：写入和清理角色拥有的菜单与按钮权限关系。
 *
 * <p>边界：不计算最终 permission code，不决定前端路由树结构。
 *
 * <p>当前阶段能力：为角色授权、当前用户权限码和动态路由提供关系数据。
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

  List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

  int deleteByRoleId(@Param("roleId") Long roleId);

  int batchInsert(@Param("roleMenus") Collection<SysRoleMenu> roleMenus);
}

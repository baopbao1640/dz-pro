package com.platform.core.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.menu.domain.SysMenu;
import com.platform.core.system.menu.dto.MenuQueryDTO;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单 Mapper，封装 `sys_menu` 查询、权限码查询和角色授权菜单查询。
 *
 * <p>职责：提供菜单树数据、权限码唯一性、子节点占用和角色菜单集合。
 *
 * <p>边界：不组装前端路由，不决定按钮权限如何展示。
 *
 * <p>当前阶段能力：支撑菜单管理、动态路由和 permission code 查询。
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

  List<SysMenu> selectMenuList(@Param("query") MenuQueryDTO query);

  SysMenu selectActiveById(@Param("id") Long id);

  Long countActivePermissionCode(
      @Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);

  Long countActiveChildren(@Param("parentId") Long parentId);

  List<SysMenu> selectEnabledMenusByRoleIds(@Param("roleIds") Collection<Long> roleIds);

  List<String> selectEnabledPermissionCodesByRoleIds(@Param("roleIds") Collection<Long> roleIds);
}

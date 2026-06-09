package com.platform.core.system.menu.service;

import com.platform.core.system.menu.dto.MenuQueryDTO;
import com.platform.core.system.menu.dto.MenuSaveDTO;
import com.platform.core.system.menu.dto.MenuStatusDTO;
import com.platform.core.system.menu.vo.MenuTreeVO;
import com.platform.core.system.menu.vo.RouteVO;
import java.util.Collection;
import java.util.List;

/**
 * 菜单与权限资源服务契约，定义菜单树、动态路由和 permission code 查询能力。
 *
 * <p>职责：维护 `sys_menu`，并按角色授权关系输出路由树和权限码。
 *
 * <p>边界：不执行当前请求授权判断，不替代 `PermissionService`。
 *
 * <p>当前阶段能力：支撑菜单管理、动态路由接口和后端权限码 enforce 数据来源。
 */
public interface SysMenuService {

  List<MenuTreeVO> tree(MenuQueryDTO query);

  MenuTreeVO detail(Long id);

  MenuTreeVO create(MenuSaveDTO dto);

  MenuTreeVO update(Long id, MenuSaveDTO dto);

  void updateStatus(Long id, MenuStatusDTO dto);

  void delete(Long id);

  List<RouteVO> routesForRoleIds(Collection<Long> roleIds);

  List<String> permissionCodesForRoleIds(Collection<Long> roleIds);
}

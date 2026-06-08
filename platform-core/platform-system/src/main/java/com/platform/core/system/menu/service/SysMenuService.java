package com.platform.core.system.menu.service;

import com.platform.core.system.menu.dto.MenuQueryDTO;
import com.platform.core.system.menu.dto.MenuSaveDTO;
import com.platform.core.system.menu.dto.MenuStatusDTO;
import com.platform.core.system.menu.vo.MenuTreeVO;
import com.platform.core.system.menu.vo.RouteVO;
import java.util.Collection;
import java.util.List;

/** Menu and permission resource service contract. */
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

package com.platform.core.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.menu.domain.SysMenu;
import com.platform.core.system.menu.dto.MenuQueryDTO;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** Persistence contract for sys_menu. */
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

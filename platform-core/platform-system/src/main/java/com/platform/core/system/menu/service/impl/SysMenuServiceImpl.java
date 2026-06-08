package com.platform.core.system.menu.service.impl;

import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.menu.domain.SysMenu;
import com.platform.core.system.menu.dto.MenuQueryDTO;
import com.platform.core.system.menu.dto.MenuSaveDTO;
import com.platform.core.system.menu.dto.MenuStatusDTO;
import com.platform.core.system.menu.enums.MenuType;
import com.platform.core.system.menu.mapper.SysMenuMapper;
import com.platform.core.system.menu.service.SysMenuService;
import com.platform.core.system.menu.vo.MenuTreeVO;
import com.platform.core.system.menu.vo.RouteVO;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** Minimal menu service implementation for Phase 3A backend scaffolding. */
@Service
public class SysMenuServiceImpl implements SysMenuService {

  private static final Long ROOT_PARENT_ID = 0L;

  private final SysMenuMapper menuMapper;

  public SysMenuServiceImpl(SysMenuMapper menuMapper) {
    this.menuMapper = menuMapper;
  }

  @Override
  public List<MenuTreeVO> tree(MenuQueryDTO query) {
    return buildMenuTree(menuMapper.selectMenuList(query));
  }

  @Override
  public MenuTreeVO detail(Long id) {
    return toTreeVO(requireMenu(id));
  }

  @Override
  @Transactional
  public MenuTreeVO create(MenuSaveDTO dto) {
    validateSave(dto, null);
    SysMenu menu = new SysMenu();
    fillMenu(menu, dto);
    menu.setDelFlag(StatusConstants.NOT_DELETED);
    menu.setCreateTime(OffsetDateTime.now());
    menuMapper.insert(menu);
    return toTreeVO(menu);
  }

  @Override
  @Transactional
  public MenuTreeVO update(Long id, MenuSaveDTO dto) {
    requireMenu(id);
    if (id.equals(dto.getParentId())) {
      throw new IllegalArgumentException("Menu cannot be its own parent");
    }
    validateSave(dto, id);
    SysMenu menu = new SysMenu();
    menu.setId(id);
    fillMenu(menu, dto);
    menu.setUpdateTime(OffsetDateTime.now());
    menuMapper.updateById(menu);
    return detail(id);
  }

  @Override
  @Transactional
  public void updateStatus(Long id, MenuStatusDTO dto) {
    requireMenu(id);
    if (!StatusConstants.NORMAL.equals(dto.getStatus())
        && !StatusConstants.DISABLED.equals(dto.getStatus())) {
      throw new IllegalArgumentException("Unsupported menu status");
    }
    SysMenu menu = new SysMenu();
    menu.setId(id);
    menu.setStatus(dto.getStatus());
    menu.setUpdateTime(OffsetDateTime.now());
    menuMapper.updateById(menu);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    requireMenu(id);
    if (menuMapper.countActiveChildren(id) > 0) {
      throw new IllegalStateException("Menu has active children");
    }
    SysMenu menu = new SysMenu();
    menu.setId(id);
    menu.setDelFlag(StatusConstants.DELETED);
    menu.setUpdateTime(OffsetDateTime.now());
    menuMapper.updateById(menu);
  }

  @Override
  public List<RouteVO> routesForRoleIds(Collection<Long> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) {
      return List.of();
    }
    List<MenuTreeVO> tree = buildMenuTree(menuMapper.selectEnabledMenusByRoleIds(roleIds));
    return tree.stream().map(this::toRouteVO).toList();
  }

  @Override
  public List<String> permissionCodesForRoleIds(Collection<Long> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) {
      return List.of();
    }
    return menuMapper.selectEnabledPermissionCodesByRoleIds(roleIds);
  }

  private void validateSave(MenuSaveDTO dto, Long excludeId) {
    if (!StringUtils.hasText(dto.getMenuName())) {
      throw new IllegalArgumentException("Menu name is required");
    }
    if (dto.getParentId() == null) {
      throw new IllegalArgumentException("Parent menu ID is required");
    }
    if (dto.getOrderNum() == null || dto.getOrderNum() < 0) {
      throw new IllegalArgumentException("Menu order must be non-negative");
    }
    if (!MenuType.supports(dto.getMenuType())) {
      throw new IllegalArgumentException("Unsupported menu type");
    }
    validateYesNo(dto.getIsFrame(), "isFrame");
    validateYesNo(dto.getIsCache(), "isCache");
    validateYesNo(dto.getVisible(), "visible");
    validateRequiredFieldsByType(dto);
    if (StringUtils.hasText(dto.getPermissionCode())
        && menuMapper.countActivePermissionCode(dto.getPermissionCode(), excludeId) > 0) {
      throw new IllegalArgumentException("Permission code already exists");
    }
  }

  private void validateRequiredFieldsByType(MenuSaveDTO dto) {
    if (MenuType.DIRECTORY.getCode().equals(dto.getMenuType())
        && !StringUtils.hasText(dto.getPath())) {
      throw new IllegalArgumentException("Directory path is required");
    }
    if (MenuType.MENU.getCode().equals(dto.getMenuType())) {
      if (!StringUtils.hasText(dto.getPath())
          || !StringUtils.hasText(dto.getComponent())
          || !StringUtils.hasText(dto.getRouteName())) {
        throw new IllegalArgumentException("Menu path, component, and route name are required");
      }
    }
    if (MenuType.FUNCTION.getCode().equals(dto.getMenuType())
        && !StringUtils.hasText(dto.getPermissionCode())) {
      throw new IllegalArgumentException("Function permission code is required");
    }
  }

  private void validateYesNo(String value, String fieldName) {
    if (!StatusConstants.YES.equals(value) && !StatusConstants.NO.equals(value)) {
      throw new IllegalArgumentException(fieldName + " must be Y or N");
    }
  }

  private SysMenu requireMenu(Long id) {
    SysMenu menu = menuMapper.selectActiveById(id);
    if (menu == null) {
      throw new IllegalArgumentException("Menu does not exist");
    }
    return menu;
  }

  private void fillMenu(SysMenu menu, MenuSaveDTO dto) {
    menu.setMenuName(dto.getMenuName());
    menu.setParentId(dto.getParentId());
    menu.setOrderNum(dto.getOrderNum());
    menu.setPath(dto.getPath());
    menu.setComponent(dto.getComponent());
    menu.setQueryParam(dto.getQueryParam());
    menu.setRouteName(dto.getRouteName());
    menu.setIsFrame(dto.getIsFrame());
    menu.setIsCache(dto.getIsCache());
    menu.setMenuType(dto.getMenuType());
    menu.setVisible(dto.getVisible());
    menu.setStatus(StringUtils.hasText(dto.getStatus()) ? dto.getStatus() : StatusConstants.NORMAL);
    menu.setPermissionCode(dto.getPermissionCode());
    menu.setIcon(dto.getIcon());
    menu.setRemark(dto.getRemark());
  }

  private List<MenuTreeVO> buildMenuTree(List<SysMenu> menus) {
    Map<Long, MenuTreeVO> nodes = new LinkedHashMap<>();
    for (SysMenu menu : menus) {
      nodes.put(menu.getId(), toTreeVO(menu));
    }
    List<MenuTreeVO> roots = new ArrayList<>();
    for (MenuTreeVO node : nodes.values()) {
      MenuTreeVO parent = nodes.get(node.getParentId());
      if (parent == null || ROOT_PARENT_ID.equals(node.getParentId())) {
        roots.add(node);
      } else {
        parent.getChildren().add(node);
      }
    }
    sortTree(roots);
    return roots;
  }

  private void sortTree(List<MenuTreeVO> nodes) {
    nodes.sort(Comparator.comparing(MenuTreeVO::getOrderNum).thenComparing(MenuTreeVO::getId));
    for (MenuTreeVO node : nodes) {
      sortTree(node.getChildren());
    }
  }

  private MenuTreeVO toTreeVO(SysMenu menu) {
    MenuTreeVO vo = new MenuTreeVO();
    vo.setId(menu.getId());
    vo.setParentId(menu.getParentId());
    vo.setMenuName(menu.getMenuName());
    vo.setMenuType(menu.getMenuType());
    vo.setPath(menu.getPath());
    vo.setComponent(menu.getComponent());
    vo.setRouteName(menu.getRouteName());
    vo.setPermissionCode(menu.getPermissionCode());
    vo.setIcon(menu.getIcon());
    vo.setVisible(menu.getVisible());
    vo.setIsCache(menu.getIsCache());
    vo.setIsFrame(menu.getIsFrame());
    vo.setStatus(menu.getStatus());
    vo.setOrderNum(menu.getOrderNum());
    return vo;
  }

  private RouteVO toRouteVO(MenuTreeVO menu) {
    RouteVO route = new RouteVO();
    route.setName(menu.getRouteName());
    route.setPath(menu.getPath());
    route.setComponent(menu.getComponent());
    RouteVO.RouteMetaVO meta = new RouteVO.RouteMetaVO();
    meta.setTitle(menu.getMenuName());
    meta.setIcon(menu.getIcon());
    meta.setHidden(StatusConstants.NO.equals(menu.getVisible()));
    meta.setKeepAlive(StatusConstants.YES.equals(menu.getIsCache()));
    route.setMeta(meta);
    route.setChildren(menu.getChildren().stream().map(this::toRouteVO).toList());
    return route;
  }
}

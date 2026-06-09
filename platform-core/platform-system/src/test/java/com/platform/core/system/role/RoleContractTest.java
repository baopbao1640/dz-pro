package com.platform.core.system.role;

import static org.assertj.core.api.Assertions.assertThat;

import com.platform.core.system.role.domain.SysRole;
import com.platform.core.system.role.dto.RoleDataScopeDTO;
import com.platform.core.system.role.dto.RoleMenuAssignDTO;
import com.platform.core.system.role.enums.RoleDataScope;
import com.platform.core.system.role.mapper.SysRoleDeptMapper;
import com.platform.core.system.role.mapper.SysRoleMapper;
import com.platform.core.system.role.mapper.SysRoleMenuMapper;
import com.platform.core.system.role.service.impl.SysRoleServiceImpl;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class RoleContractTest {

  @Test
  void roleDataScopeExposesRequiredPrdValues() {
    assertThat(RoleDataScope.ALL.getCode()).isEqualTo("1");
    assertThat(RoleDataScope.CUSTOM.getCode()).isEqualTo("2");
    assertThat(RoleDataScope.DEPT.getCode()).isEqualTo("3");
    assertThat(RoleDataScope.DEPT_AND_CHILD.getCode()).isEqualTo("4");
    assertThat(RoleDataScope.SELF.getCode()).isEqualTo("5");
  }

  @Test
  void roleAssignmentContractsPreserveStrictAndHalfCheckedSemantics() {
    RoleMenuAssignDTO menuAssign = new RoleMenuAssignDTO();
    menuAssign.setMenuIds(List.of(11L, 12L));
    menuAssign.setHalfCheckedMenuIds(List.of(10L));
    menuAssign.setMenuCheckStrictly("N");

    RoleDataScopeDTO dataScope = new RoleDataScopeDTO();
    dataScope.setDataScope(RoleDataScope.CUSTOM.getCode());
    dataScope.setDeptIds(List.of(21L, 22L));
    dataScope.setDeptCheckStrictly("Y");

    assertThat(menuAssign.getMenuIds()).containsExactly(11L, 12L);
    assertThat(menuAssign.getHalfCheckedMenuIds()).containsExactly(10L);
    assertThat(menuAssign.getMenuCheckStrictly()).isEqualTo("N");
    assertThat(dataScope.getDataScope()).isEqualTo("2");
    assertThat(dataScope.getDeptIds()).containsExactly(21L, 22L);
    assertThat(dataScope.getDeptCheckStrictly()).isEqualTo("Y");
  }

  @Test
  void assignMenusPersistsOnlyFullyCheckedMenuIds() {
    SysRoleMapper roleMapper = Mockito.mock(SysRoleMapper.class);
    SysRoleMenuMapper roleMenuMapper = Mockito.mock(SysRoleMenuMapper.class);
    SysRoleDeptMapper roleDeptMapper = Mockito.mock(SysRoleDeptMapper.class);
    SysRole role = new SysRole();
    role.setId(1L);
    Mockito.when(roleMapper.selectActiveById(1L)).thenReturn(role);
    SysRoleServiceImpl service = new SysRoleServiceImpl(roleMapper, roleMenuMapper, roleDeptMapper);
    RoleMenuAssignDTO dto = new RoleMenuAssignDTO();
    dto.setMenuIds(List.of(11L, 12L));
    dto.setHalfCheckedMenuIds(List.of(10L));
    dto.setMenuCheckStrictly("N");

    service.assignMenus(1L, dto);

    ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
    Mockito.verify(roleMenuMapper).deleteByRoleId(1L);
    Mockito.verify(roleMenuMapper).batchInsert(captor.capture());
    assertThat(captor.getValue()).hasSize(2);
  }
}

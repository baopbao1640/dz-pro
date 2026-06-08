package com.platform.core.system.dept.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.dept.domain.SysDept;
import com.platform.core.system.dept.dto.DeptSaveDTO;
import com.platform.core.system.dept.mapper.SysDeptMapper;
import com.platform.core.system.dept.vo.DeptTreeVO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SysDeptServiceImplTest {

  @Test
  void createDeptDerivesAncestorsFromParent() {
    SysDeptMapper mapper = org.mockito.Mockito.mock(SysDeptMapper.class);
    SysDept parent = new SysDept();
    parent.setId(10L);
    parent.setAncestors("0,1");
    parent.setStatus(StatusConstants.NORMAL);
    when(mapper.selectActiveById(10L)).thenReturn(parent);
    when(mapper.countActiveByParentAndName(10L, "Engineering", null)).thenReturn(0L);

    SysDeptServiceImpl service = new SysDeptServiceImpl(mapper);
    DeptSaveDTO dto = new DeptSaveDTO();
    dto.setParentId(10L);
    dto.setDeptName("Engineering");
    dto.setOrderNum(1);
    dto.setStatus(StatusConstants.NORMAL);

    DeptTreeVO created = service.create(dto);

    ArgumentCaptor<SysDept> captor = ArgumentCaptor.forClass(SysDept.class);
    verify(mapper).insert(captor.capture());
    assertThat(captor.getValue().getAncestors()).isEqualTo("0,1,10");
    assertThat(captor.getValue().getDelFlag()).isEqualTo(StatusConstants.NOT_DELETED);
    assertThat(created.getDeptName()).isEqualTo("Engineering");
  }

  @Test
  void buildTreeNestsChildrenUnderParents() {
    SysDeptMapper mapper = org.mockito.Mockito.mock(SysDeptMapper.class);
    SysDept parent = new SysDept();
    parent.setId(1L);
    parent.setParentId(0L);
    parent.setDeptName("Headquarters");
    parent.setOrderNum(1);
    parent.setStatus(StatusConstants.NORMAL);
    SysDept child = new SysDept();
    child.setId(2L);
    child.setParentId(1L);
    child.setDeptName("Engineering");
    child.setOrderNum(1);
    child.setStatus(StatusConstants.NORMAL);
    when(mapper.selectDeptList(any())).thenReturn(List.of(parent, child));

    SysDeptServiceImpl service = new SysDeptServiceImpl(mapper);

    List<DeptTreeVO> tree = service.tree(null);

    assertThat(tree).hasSize(1);
    assertThat(tree.get(0).getChildren()).hasSize(1);
    assertThat(tree.get(0).getChildren().get(0).getDeptName()).isEqualTo("Engineering");
  }
}

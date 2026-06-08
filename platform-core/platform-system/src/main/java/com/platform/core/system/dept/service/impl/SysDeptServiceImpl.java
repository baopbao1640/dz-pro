package com.platform.core.system.dept.service.impl;

import com.platform.core.common.constant.StatusConstants;
import com.platform.core.system.dept.domain.SysDept;
import com.platform.core.system.dept.dto.DeptQueryDTO;
import com.platform.core.system.dept.dto.DeptSaveDTO;
import com.platform.core.system.dept.dto.DeptSortDTO;
import com.platform.core.system.dept.dto.DeptStatusDTO;
import com.platform.core.system.dept.mapper.SysDeptMapper;
import com.platform.core.system.dept.service.SysDeptService;
import com.platform.core.system.dept.vo.DeptTreeVO;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysDeptServiceImpl implements SysDeptService {

  private static final long ROOT_PARENT_ID = 0L;
  private static final String ROOT_ANCESTORS = "0";

  private final SysDeptMapper deptMapper;

  public SysDeptServiceImpl(SysDeptMapper deptMapper) {
    this.deptMapper = deptMapper;
  }

  @Override
  public List<DeptTreeVO> tree(DeptQueryDTO query) {
    List<SysDept> depts = deptMapper.selectDeptList(query);
    Map<Long, DeptTreeVO> nodeById = new LinkedHashMap<>();
    for (SysDept dept : depts) {
      nodeById.put(dept.getId(), toTreeVO(dept));
    }
    List<DeptTreeVO> roots = new ArrayList<>();
    for (DeptTreeVO node : nodeById.values()) {
      DeptTreeVO parent = nodeById.get(node.getParentId());
      if (parent == null) {
        roots.add(node);
      } else {
        parent.getChildren().add(node);
      }
    }
    return roots;
  }

  @Override
  public DeptTreeVO detail(Long id) {
    return toTreeVO(requireActive(id));
  }

  @Override
  @Transactional
  public DeptTreeVO create(DeptSaveDTO dto) {
    ensureUniqueName(dto.getParentId(), dto.getDeptName(), null);
    SysDept dept = new SysDept();
    applySave(dept, dto);
    dept.setAncestors(resolveAncestors(dto.getParentId()));
    dept.setDelFlag(StatusConstants.NOT_DELETED);
    dept.setCreateTime(OffsetDateTime.now());
    deptMapper.insert(dept);
    return toTreeVO(dept);
  }

  @Override
  @Transactional
  public DeptTreeVO update(Long id, DeptSaveDTO dto) {
    requireActive(id);
    ensureUniqueName(dto.getParentId(), dto.getDeptName(), id);
    SysDept dept = new SysDept();
    dept.setId(id);
    applySave(dept, dto);
    dept.setAncestors(resolveAncestors(dto.getParentId()));
    dept.setUpdateTime(OffsetDateTime.now());
    deptMapper.updateById(dept);
    return detail(id);
  }

  @Override
  @Transactional
  public void changeStatus(Long id, DeptStatusDTO dto) {
    requireActive(id);
    if (StatusConstants.DISABLED.equals(dto.getStatus())
        && deptMapper.countEnabledChildren(id) > 0) {
      throw new IllegalStateException("Department has enabled children");
    }
    SysDept dept = new SysDept();
    dept.setId(id);
    dept.setStatus(defaultStatus(dto.getStatus()));
    dept.setUpdateTime(OffsetDateTime.now());
    deptMapper.updateById(dept);
  }

  @Override
  @Transactional
  public void sort(DeptSortDTO dto) {
    for (DeptSortDTO.Item item : dto.getItems()) {
      SysDept dept = new SysDept();
      dept.setId(item.getId());
      dept.setParentId(item.getParentId());
      dept.setOrderNum(item.getOrderNum());
      dept.setUpdateTime(OffsetDateTime.now());
      deptMapper.updateById(dept);
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {
    requireActive(id);
    if (deptMapper.countActiveChildren(id) > 0) {
      throw new IllegalStateException("Department has active children");
    }
    if (deptMapper.countActiveUsers(id) > 0) {
      throw new IllegalStateException("Department has active users");
    }
    SysDept dept = new SysDept();
    dept.setId(id);
    dept.setDelFlag(StatusConstants.DELETED);
    dept.setUpdateTime(OffsetDateTime.now());
    deptMapper.updateById(dept);
  }

  private void applySave(SysDept dept, DeptSaveDTO dto) {
    dept.setParentId(dto.getParentId());
    dept.setDeptName(dto.getDeptName());
    dept.setOrderNum(dto.getOrderNum());
    dept.setLeaderUserId(dto.getLeaderUserId());
    dept.setPhone(dto.getPhone());
    dept.setEmail(dto.getEmail());
    dept.setStatus(defaultStatus(dto.getStatus()));
    dept.setRemark(dto.getRemark());
  }

  private SysDept requireActive(Long id) {
    SysDept dept = deptMapper.selectActiveById(id);
    if (dept == null) {
      throw new IllegalArgumentException("Department does not exist");
    }
    return dept;
  }

  private void ensureUniqueName(Long parentId, String deptName, Long excludeId) {
    if (deptMapper.countActiveByParentAndName(parentId, deptName, excludeId) > 0) {
      throw new IllegalArgumentException("Department name already exists under parent");
    }
  }

  private String resolveAncestors(Long parentId) {
    if (parentId == null || ROOT_PARENT_ID == parentId) {
      return ROOT_ANCESTORS;
    }
    SysDept parent = deptMapper.selectActiveById(parentId);
    if (parent == null || !StatusConstants.NORMAL.equals(parent.getStatus())) {
      throw new IllegalArgumentException("Parent department is not available");
    }
    return parent.getAncestors() + "," + parent.getId();
  }

  private String defaultStatus(String status) {
    return status == null ? StatusConstants.NORMAL : status;
  }

  private DeptTreeVO toTreeVO(SysDept dept) {
    DeptTreeVO vo = new DeptTreeVO();
    vo.setId(dept.getId());
    vo.setParentId(dept.getParentId());
    vo.setAncestors(dept.getAncestors());
    vo.setDeptName(dept.getDeptName());
    vo.setOrderNum(dept.getOrderNum());
    vo.setLeaderUserId(dept.getLeaderUserId());
    vo.setPhone(dept.getPhone());
    vo.setEmail(dept.getEmail());
    vo.setStatus(dept.getStatus());
    return vo;
  }
}

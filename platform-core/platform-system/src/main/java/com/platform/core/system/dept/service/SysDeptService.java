package com.platform.core.system.dept.service;

import com.platform.core.system.dept.dto.DeptQueryDTO;
import com.platform.core.system.dept.dto.DeptSaveDTO;
import com.platform.core.system.dept.dto.DeptSortDTO;
import com.platform.core.system.dept.dto.DeptStatusDTO;
import com.platform.core.system.dept.vo.DeptTreeVO;
import java.util.List;

/**
 * 部门管理服务契约，定义组织树维护和校验能力。
 *
 * <p>职责：维护部门树、状态、排序和删除约束。
 *
 * <p>边界：不处理用户授权，不直接实现数据权限 SQL 注入。
 *
 * <p>当前阶段能力：为用户部门归属和后续 data scope 部门树计算提供基础数据。
 */
public interface SysDeptService {

  List<DeptTreeVO> tree(DeptQueryDTO query);

  DeptTreeVO detail(Long id);

  DeptTreeVO create(DeptSaveDTO dto);

  DeptTreeVO update(Long id, DeptSaveDTO dto);

  void changeStatus(Long id, DeptStatusDTO dto);

  void sort(DeptSortDTO dto);

  void delete(Long id);
}

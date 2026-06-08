package com.platform.core.system.dept.service;

import com.platform.core.system.dept.dto.DeptQueryDTO;
import com.platform.core.system.dept.dto.DeptSaveDTO;
import com.platform.core.system.dept.dto.DeptSortDTO;
import com.platform.core.system.dept.dto.DeptStatusDTO;
import com.platform.core.system.dept.vo.DeptTreeVO;
import java.util.List;

public interface SysDeptService {

  List<DeptTreeVO> tree(DeptQueryDTO query);

  DeptTreeVO detail(Long id);

  DeptTreeVO create(DeptSaveDTO dto);

  DeptTreeVO update(Long id, DeptSaveDTO dto);

  void changeStatus(Long id, DeptStatusDTO dto);

  void sort(DeptSortDTO dto);

  void delete(Long id);
}

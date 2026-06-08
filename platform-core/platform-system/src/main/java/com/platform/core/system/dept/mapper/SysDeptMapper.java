package com.platform.core.system.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.dept.domain.SysDept;
import com.platform.core.system.dept.dto.DeptQueryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

  SysDept selectActiveById(@Param("id") Long id);

  List<SysDept> selectDeptList(@Param("query") DeptQueryDTO query);

  long countActiveChildren(@Param("id") Long id);

  long countEnabledChildren(@Param("id") Long id);

  long countActiveUsers(@Param("id") Long id);

  long countActiveByParentAndName(
      @Param("parentId") Long parentId,
      @Param("deptName") String deptName,
      @Param("excludeId") Long excludeId);
}

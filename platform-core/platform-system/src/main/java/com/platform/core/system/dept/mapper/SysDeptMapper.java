package com.platform.core.system.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.core.system.dept.domain.SysDept;
import com.platform.core.system.dept.dto.DeptQueryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 部门 Mapper，封装 `sys_dept` 组织树查询和约束检查。
 *
 * <p>职责：提供部门树列表、启用子节点、用户占用和同级名称唯一性查询。
 *
 * <p>边界：不生成数据权限条件，不决定部门树业务校验结果。
 *
 * <p>当前阶段能力：支撑部门管理 CRUD 和后续 data scope 部门关系计算。
 */
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

package com.platform.core.framework.security.authz;

import com.platform.core.framework.security.annotation.DataPermission;
import com.platform.core.framework.security.context.CurrentUser;

/**
 * 数据权限条件提供者，由业务模块根据当前用户和 `@DataPermission` 生成条件契约。
 *
 * <p>职责：把用户数据范围转换为可应用到查询层的条件。
 *
 * <p>边界：framework 只定义接口，不读取部门、角色或用户表。
 *
 * <p>当前阶段能力：system 模块可提供基础条件实现。
 */
/*
 * Boundary:
 * 业务模块负责解释 ALL、CUSTOM、DEPT、DEPT_AND_CHILD、SELF 的实际数据边界。
 */
/*
 * Deferred:
 * 条件生成后的全局 SQL 应用点暂未统一，后续需要 MyBatis 插件或 AOP 覆盖核心查询。
 */
/*
 * Risk:
 * 如果只生成条件但未应用到 Mapper，接口存在但数据权限不真正生效。
 */
public interface DataScopeConditionProvider {

  /**
   * 为当前用户构建数据范围条件。
   *
   * <p>关键规则：`DataPermission` 提供表别名和字段名，业务实现提供具体范围。
   *
   * <p>返回含义：返回可用于查询收敛的数据权限条件。
   */
  DataScopeCondition buildCondition(CurrentUser currentUser, DataPermission dataPermission);
}

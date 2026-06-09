package com.platform.core.system.auth.service;

import com.platform.core.framework.security.annotation.DataPermission;
import com.platform.core.framework.security.authz.DataScope;
import com.platform.core.framework.security.authz.DataScopeCondition;
import com.platform.core.framework.security.authz.DataScopeConditionProvider;
import com.platform.core.framework.security.context.CurrentUser;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * system 数据权限条件提供器，基于当前用户 data scope 和 `@DataPermission` 生成查询条件契约。
 *
 * <p>职责：把 ALL、DEPT、SELF 等数据范围转换为 framework `DataScopeCondition`。
 *
 * <p>边界：当前只生成条件，不负责把条件全局注入所有 Mapper SQL。
 *
 * <p>当前阶段能力：支持超级管理员/ALL 无限制、SELF 按用户字段、其他范围按部门字段收敛。
 */
/*
 * Boundary:
 * 该服务不修改 SQL，只提供条件；真正应用点仍在 Mapper/AOP/MyBatis 层。
 */
/*
 * Deferred:
 * CUSTOM 和 DEPT_AND_CHILD 暂未展开部门集合，后续需要结合部门树补齐。
 */
/*
 * Risk:
 * 如果调用方未把返回条件应用到查询，数据权限不会实际生效。
 */
@Service
public class SystemDataScopeConditionProvider implements DataScopeConditionProvider {

  /**
   * 构建当前用户的数据范围条件。
   *
   * <p>关键规则：super admin 和 ALL 返回无限制；SELF 使用用户字段；其他范围先按部门字段处理。
   *
   * <p>返回含义：返回可被查询层消费的条件契约。
   */
  @Override
  public DataScopeCondition buildCondition(CurrentUser currentUser, DataPermission dataPermission) {
    DataScope scope = DataScope.fromCode(currentUser.dataScope()).orElse(DataScope.SELF);
    if (currentUser.superAdmin() || scope == DataScope.ALL) {
      return DataScopeCondition.unrestricted();
    }
    if (scope == DataScope.SELF) {
      return new DataScopeCondition(
          column(dataPermission, dataPermission.userColumn()) + " = ?",
          List.of(currentUser.userId()));
    }
    return new DataScopeCondition(
        column(dataPermission, dataPermission.deptColumn()) + " = ?",
        List.of(currentUser.deptId()));
  }

  private String column(DataPermission dataPermission, String columnName) {
    if (dataPermission.tableAlias() == null || dataPermission.tableAlias().isBlank()) {
      return columnName;
    }
    return dataPermission.tableAlias() + "." + columnName;
  }
}

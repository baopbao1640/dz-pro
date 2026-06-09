package com.platform.core.system.auth.service;

import com.platform.core.framework.security.annotation.DataPermission;
import com.platform.core.framework.security.authz.DataScope;
import com.platform.core.framework.security.authz.DataScopeCondition;
import com.platform.core.framework.security.authz.DataScopeConditionProvider;
import com.platform.core.framework.security.context.CurrentUser;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 初版 data scope 只生成可测试条件契约，不直接注入所有 Mapper SQL。这样可以先验证 ALL / DEPT / SELF 等语义，再在后续用 AOP 或 MyBatis
 * 插件统一落地。
 */
@Service
public class SystemDataScopeConditionProvider implements DataScopeConditionProvider {

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

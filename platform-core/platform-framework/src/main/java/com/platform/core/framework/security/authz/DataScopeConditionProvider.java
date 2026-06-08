package com.platform.core.framework.security.authz;

import com.platform.core.framework.security.annotation.DataPermission;
import com.platform.core.framework.security.context.CurrentUser;

/**
 * 数据权限条件提供者由业务侧实现具体规则。framework 保留接口，是为了让 ALL、CUSTOM、DEPT、 DEPT_AND_CHILD、SELF 的 SQL 策略可以在 system
 * 模块结合部门树和当前用户解释。
 */
public interface DataScopeConditionProvider {

  DataScopeCondition buildCondition(CurrentUser currentUser, DataPermission dataPermission);
}

package com.platform.core.framework.security.authz;

/** 权限匹配模式用于表达一个入口需要全部权限还是任一权限。保留显式枚举，是为了让 annotation 语义在后续 AOP 强制校验时可读且可测试。 */
public enum PermissionMatchMode {
  ALL,
  ANY
}

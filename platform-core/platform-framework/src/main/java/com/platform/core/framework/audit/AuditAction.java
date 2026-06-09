package com.platform.core.framework.audit;

/** 审计动作使用稳定字符串编码，是为了和 `sys_oper_log`、前端筛选条件及后续报表保持兼容。 新增动作必须评估历史日志查询和字典数据是否需要同步扩展。 */
public enum AuditAction {
  CREATE("create"),
  UPDATE("update"),
  DELETE("delete"),
  ENABLE("enable"),
  DISABLE("disable"),
  ASSIGN("assign"),
  REVOKE("revoke");

  private final String code;

  AuditAction(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}

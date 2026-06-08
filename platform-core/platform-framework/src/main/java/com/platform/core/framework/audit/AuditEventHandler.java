package com.platform.core.framework.audit;

/**
 * 审计事件处理扩展点。接口放在 framework，是为了避免业务方法直接依赖日志表； 持久化实现应放在 system 模块，保持 framework 不反向依赖 `sys_oper_log`。
 */
@FunctionalInterface
public interface AuditEventHandler {

  void handle(AuditEvent event) throws Exception;
}

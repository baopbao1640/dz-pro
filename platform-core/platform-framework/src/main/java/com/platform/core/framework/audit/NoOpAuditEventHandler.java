package com.platform.core.framework.audit;

import org.springframework.stereotype.Component;

/**
 * Deferred: 当前提供 no-op fallback，是为了让审计 annotation 和发布管道可以先被业务代码引用。 后续应在 system 模块提供真实
 * `AuditEventHandler`，异步写入 `sys_oper_log` 或 `sys_login_log`。
 */
@Component
public class NoOpAuditEventHandler implements AuditEventHandler {

  @Override
  public void handle(AuditEvent event) {
    // Deferred: 持久化属于后续 system audit slice；这里保持空实现以避免 framework 依赖日志表。
  }
}

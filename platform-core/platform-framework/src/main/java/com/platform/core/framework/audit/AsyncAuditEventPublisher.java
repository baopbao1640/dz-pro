package com.platform.core.framework.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 审计发布器放在 framework，是为了给业务模块提供统一入口；真正写入 `sys_oper_log` 或 `sys_login_log` 的实现由 system
 * 模块提供。这里使用独立线程池，是因为审计失败不能阻断主流程。
 */
@Component
public class AsyncAuditEventPublisher implements AuditEventPublisher {

  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAuditEventPublisher.class);

  private final TaskExecutor auditTaskExecutor;
  private final AuditEventHandler auditEventHandler;

  public AsyncAuditEventPublisher(
      @Qualifier(AuditAsyncConfig.AUDIT_TASK_EXECUTOR_BEAN) TaskExecutor auditTaskExecutor,
      AuditEventHandler auditEventHandler) {
    this.auditTaskExecutor = auditTaskExecutor;
    this.auditEventHandler = auditEventHandler;
  }

  @Override
  public void publish(AuditEvent event) {
    auditTaskExecutor.execute(
        () -> {
          try {
            auditEventHandler.handle(event);
          } catch (Exception ex) {
            // Risk: 审计异常被降级为 warn，保证主流程可用；后续需要监控指标暴露失败率。
            LOGGER.warn("Audit event handling failed and was ignored: {}", event.action(), ex);
          }
        });
  }
}

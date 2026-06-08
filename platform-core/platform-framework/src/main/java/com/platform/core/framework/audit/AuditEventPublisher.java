package com.platform.core.framework.audit;

/** 审计发布契约要求非阻塞，是因为审计属于旁路能力。调用方不能依赖发布结果决定业务事务是否提交。 */
public interface AuditEventPublisher {

  void publish(AuditEvent event);
}

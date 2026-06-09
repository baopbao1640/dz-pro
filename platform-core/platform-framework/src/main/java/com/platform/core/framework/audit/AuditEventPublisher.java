package com.platform.core.framework.audit;

/**
 * 审计事件发布契约，提供业务入口到审计处理器之间的非阻塞边界。
 *
 * <p>职责：接收已构造的 `AuditEvent` 并交给后续处理管道。
 *
 * <p>边界：调用方不能依赖发布结果决定业务事务是否提交。
 *
 * <p>当前阶段能力：由异步发布器转交给 system 侧 handler。
 */
/*
 * Boundary:
 * 发布契约不定义事务一致性和日志表结构，只保证事件可被提交给处理器。
 */
/*
 * Deferred:
 * 暂不提供重试、死信队列或监控指标；后续在审计可靠性专项中补齐。
 */
/*
 * Risk:
 * 异步发布失败可能导致审计缺失，当前通过日志告警兜底，不能作为强一致审计依据。
 */
public interface AuditEventPublisher {

  void publish(AuditEvent event);
}

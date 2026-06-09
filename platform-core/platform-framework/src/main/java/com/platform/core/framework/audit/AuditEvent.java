package com.platform.core.framework.audit;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * 审计事件值对象，承载业务入口进入异步审计管道所需的最小信息。
 *
 * <p>职责：描述审计动作、模块、状态、时间、参数和结果。
 *
 * <p>边界：framework 只承载事件契约，不假设最终写入哪张日志表。
 *
 * <p>当前阶段能力：支持 system 模块写入 `sys_oper_log`。
 */
/*
 * Boundary:
 * 事件字段必须在发布前完成脱敏和长度控制，持久化策略由业务模块决定。
 */
/*
 * Deferred:
 * 登录日志和安全审计日志暂不在 framework 区分事件子类型，后续可按审计域扩展。
 */
/*
 * Risk:
 * 如果调用方传入未脱敏文本，日志表会扩大敏感信息暴露面；后续应持续强化发布前脱敏。
 */
public record AuditEvent(
    AuditAction action,
    String moduleTitle,
    String status,
    OffsetDateTime operTime,
    String traceId,
    String createBy,
    String operParam,
    String jsonResult) {

  public AuditEvent {
    Objects.requireNonNull(action, "action must not be null");
    if (operTime == null) {
      operTime = OffsetDateTime.now();
    }
  }
}

package com.platform.core.framework.audit;

import java.time.OffsetDateTime;
import java.util.Objects;

/** 审计事件进入异步管道前必须已经脱敏。framework 只承载事件契约，不假设最终写入哪张日志表， 这样可以让 system 模块分别扩展操作日志、登录日志或后续安全审计日志。 */
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

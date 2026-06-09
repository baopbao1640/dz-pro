package com.platform.core.framework.audit;

import com.platform.core.common.constant.StatusConstants;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 审计切面，负责把标记了 `@AuditLog` 的业务入口转换为标准审计事件。
 *
 * <p>职责：围绕方法执行采集动作、模块、参数、结果和异常状态。
 *
 * <p>边界：本切面不直接写表，不决定审计事件最终持久化目标。
 *
 * <p>当前阶段能力：支持操作审计事件异步发布，并在业务异常时仍记录失败状态。
 */
/*
 * Boundary:
 * framework 只发布事件，system 模块负责解释当前用户并写入 `sys_oper_log`。
 */
/*
 * Deferred:
 * 暂不采集 traceId、IP、user-agent 等运行时上下文；后续可通过请求上下文扩展。
 */
/*
 * Risk:
 * 结果和异常文本通过字符串化进入审计，必须依赖 `AuditSanitizer` 控制敏感字段和长度。
 */
@Aspect
@Component
public class AuditLogAspect {

  private final AuditEventPublisher auditEventPublisher;

  public AuditLogAspect(AuditEventPublisher auditEventPublisher) {
    this.auditEventPublisher = auditEventPublisher;
  }

  /**
   * 执行被审计方法并发布成功或失败审计事件。
   *
   * <p>关键规则：业务方法原始异常必须继续抛出，审计不能吞掉业务失败。
   *
   * <p>安全影响：参数和结果进入事件前必须脱敏，避免审计日志暴露 credential。
   *
   * <p>返回含义：返回被拦截方法的原始返回值。
   */
  @Around(
      "@within(com.platform.core.framework.audit.AuditLog) || "
          + "@annotation(com.platform.core.framework.audit.AuditLog)")
  public Object publishAuditEvent(ProceedingJoinPoint joinPoint) throws Throwable {
    AuditLog auditLog = resolveAnnotation(joinPoint);
    if (auditLog == null) {
      return joinPoint.proceed();
    }
    try {
      Object result = joinPoint.proceed();
      publish(joinPoint, auditLog, StatusConstants.NORMAL, result, null);
      return result;
    } catch (Throwable ex) {
      publish(joinPoint, auditLog, StatusConstants.DISABLED, null, ex);
      throw ex;
    }
  }

  private void publish(
      ProceedingJoinPoint joinPoint,
      AuditLog auditLog,
      String status,
      Object result,
      Throwable throwable) {
    auditEventPublisher.publish(
        new AuditEvent(
            auditLog.action(),
            auditLog.moduleTitle(),
            status,
            OffsetDateTime.now(),
            null,
            null,
            AuditSanitizer.sanitize(argumentMap(joinPoint)),
            AuditSanitizer.sanitizeText(
                throwable == null ? String.valueOf(result) : throwable.getMessage())));
  }

  private Map<String, Object> argumentMap(ProceedingJoinPoint joinPoint) {
    Map<String, Object> values = new LinkedHashMap<>();
    Object[] args = joinPoint.getArgs();
    for (int i = 0; i < args.length; i++) {
      values.put("arg" + i, args[i]);
    }
    return values;
  }

  private AuditLog resolveAnnotation(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    AuditLog methodAnnotation =
        AnnotationUtils.findAnnotation(signature.getMethod(), AuditLog.class);
    if (methodAnnotation != null) {
      return methodAnnotation;
    }
    return AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), AuditLog.class);
  }
}

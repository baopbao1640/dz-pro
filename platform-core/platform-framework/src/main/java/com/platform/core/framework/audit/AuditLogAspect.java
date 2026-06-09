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

/** 审计切面只负责把业务入口转换为事件，不直接写表。这样审计失败可以由异步发布器降级处理，同时避免 framework 依赖 system 日志表。 */
@Aspect
@Component
public class AuditLogAspect {

  private final AuditEventPublisher auditEventPublisher;

  public AuditLogAspect(AuditEventPublisher auditEventPublisher) {
    this.auditEventPublisher = auditEventPublisher;
  }

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

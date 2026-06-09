package com.platform.core.framework.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要审计的变更入口。annotation 放在 framework，是为了统一语义；实际切面和持久化 Deferred 到后续阶段，避免在权限、事务和日志表边界未稳定前过早强制全局拦截。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

  String moduleTitle();

  AuditAction action();
}

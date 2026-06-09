package com.platform.core.framework.security.authz;

import com.platform.core.framework.security.annotation.RequiresPermission;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * 权限注解切面，负责让 `@RequiresPermission` 在 Controller 和 Service 入口统一生效。
 *
 * <p>职责：解析类级或方法级权限注解，并调用 `PermissionService` 执行 ANY/ALL 匹配。
 *
 * <p>边界：权限码来源仍由 system 菜单、角色和当前用户上下文提供，framework 只执行匹配语义。
 *
 * <p>当前阶段能力：无权限时抛出 `AccessDeniedException`，由安全/异常处理器输出 403 JSON。
 */
/*
 * Boundary:
 * 只处理显式标注的入口；未标注方法仍依赖路径认证和业务层主动限制。
 */
/*
 * Deferred:
 * 暂未提供权限缓存失效、审计联动和细粒度拒绝原因；后续按权限专项扩展。
 */
/*
 * Risk:
 * 如果业务入口漏标注权限码，AOP 不会自动发现；Review 和检查脚本需要持续覆盖 Controller/Service。
 */
@Aspect
@Component
public class RequiresPermissionAspect {

  private final PermissionService permissionService;

  public RequiresPermissionAspect(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  /**
   * 在目标方法执行前校验权限码。
   *
   * <p>关键规则：注解为空时不做权限码校验；ANY 匹配任一权限，ALL 要求全部权限。
   *
   * <p>安全影响：校验失败会阻断业务方法执行并返回 403。
   */
  @Before(
      "@within(com.platform.core.framework.security.annotation.RequiresPermission) || "
          + "@annotation(com.platform.core.framework.security.annotation.RequiresPermission)")
  public void checkPermission(JoinPoint joinPoint) {
    RequiresPermission annotation = resolveAnnotation(joinPoint);
    if (annotation == null || annotation.value().length == 0) {
      return;
    }
    boolean allowed =
        annotation.matchMode() == PermissionMatchMode.ANY
            ? permissionService.hasAnyPermission(Arrays.asList(annotation.value()))
            : Arrays.stream(annotation.value()).allMatch(permissionService::hasPermission);
    if (!allowed) {
      throw new AccessDeniedException("permission denied");
    }
  }

  private RequiresPermission resolveAnnotation(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    RequiresPermission methodAnnotation =
        AnnotationUtils.findAnnotation(signature.getMethod(), RequiresPermission.class);
    if (methodAnnotation != null) {
      return methodAnnotation;
    }
    return AnnotationUtils.findAnnotation(
        joinPoint.getTarget().getClass(), RequiresPermission.class);
  }
}

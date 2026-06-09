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
 * `@RequiresPermission` 在 framework 统一生效，是为了让 Controller 和 Service 不各自手写权限判断。权限来源仍由 system
 * 提供，framework 只执行匹配语义。
 */
@Aspect
@Component
public class RequiresPermissionAspect {

  private final PermissionService permissionService;

  public RequiresPermissionAspect(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

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

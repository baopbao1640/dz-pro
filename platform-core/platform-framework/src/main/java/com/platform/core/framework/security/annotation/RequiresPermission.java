package com.platform.core.framework.security.annotation;

import com.platform.core.framework.security.authz.PermissionMatchMode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 权限 annotation 表达业务入口需要的权限码。framework 只定义匹配语义，权限码来源仍由 system 菜单和角色授权提供，避免 framework 读取菜单表。 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {

  String[] value();

  PermissionMatchMode matchMode() default PermissionMatchMode.ALL;
}

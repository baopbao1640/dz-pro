package com.platform.core.framework.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限 annotation 只描述“需要按哪个表别名和字段收敛数据范围”。SQL 拼接和用户数据范围解析 Deferred 到后续拦截器或 AOP，避免在 framework 中直接耦合具体
 * Mapper。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {

  String tableAlias() default "";

  String deptColumn() default "dept_id";

  String userColumn() default "create_by";
}

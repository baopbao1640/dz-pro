# Code Comment Standardization Spec

## 目标

本阶段目标是“注释标准化实施”，不是继续制定抽象规范。基于已提交的 `governance/comments/code-comment-policy.md` 和 `specs/004-code-comment-governance/`，对当前平台底座进行可移交、可维护、可强制检查的代码注释标准化。

本阶段只允许注释改写、注释规范文件更新、检查脚本新增、`progress.md` 更新和必要文档更新，不新增业务功能，不修改业务逻辑，不扩大功能范围。

## 原则

注释标准化参考阿里巴巴 Java 开发手册中“注释要准确反映设计思想、业务逻辑、代码意图”的思想。项目采用“有价值注释强制化”，不是机械地给所有代码写废话注释。

注释必须解释：

- 职责边界。
- 设计原因。
- 业务规则。
- 安全与权限影响。
- Deferred、Risk、Debt 和 Extension。

禁止为简单赋值、简单返回、简单 getter/setter、简单 DTO/VO 字段访问方法补复述型注释。

## 范围

### 006A：platform-framework 注释标准化

处理：

- `platform-core/platform-framework/src/main/java/com/platform/core/framework/config/`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/audit/`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/web/`

必须覆盖：

- `SecurityConfig`
- `GlobalExceptionHandler`
- `RestAuthenticationEntryPoint`
- `RestAccessDeniedHandler`
- `SecurityJsonResponseWriter`
- `RequiresPermissionAspect`
- `CurrentUser`
- `CurrentUserProvider`
- `AuditLogAspect`
- `AuditEvent`
- `AuditEventPublisher`
- DataScope 相关类
- DynamicRoute 相关类

### 006B：platform-system 核心注释标准化

处理：

- `platform-core/platform-system/src/main/java/com/platform/core/system/auth/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/audit/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/user/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/role/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/menu/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/dept/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/post/`

重点覆盖 Provider、权限服务、动态路由、数据权限条件提供器、审计处理器、Controller、ServiceImpl 和 Mapper 接口。

### 006C：前端注释标准化

处理：

- `platform-ui/src/router/`
- `platform-ui/src/permissions/`
- `platform-ui/src/store/`
- `platform-ui/src/api/system/`
- `platform-ui/src/views/system/`

重点说明动态路由注入、按钮权限判断、API client 边界、401/403 状态处理和 Ant Design Vue 后台页面结构。

## 强制注释规则

以下 Java 类必须有中文类头 Javadoc：

- Controller
- Service
- ServiceImpl
- Config
- Aspect
- Annotation
- Provider
- Handler
- Interceptor
- Resolver
- Context
- Route
- Audit
- Security
- DataScope
- Permission
- Mapper 接口

类头 Javadoc 必须说明职责、边界、当前阶段能力、Deferred 和 Risk。没有 Deferred 或 Risk 时可以明确写“当前无额外 Deferred”或“当前主要风险见方法级注释”，但不能只写一句低价值描述。

以下方法必须有中文 Javadoc：

- public controller method
- public service method
- public provider method
- public permission/authz method
- public data-scope method
- public audit method
- public dynamic-route method
- public security/exception handler method
- 复杂 private helper method

方法 Javadoc 必须说明方法目的、关键业务规则、边界条件、权限或安全影响、返回含义和异常行为。

## Boundary / Deferred / Risk 格式

重点区域必须使用统一块注释格式：

```java
/*
 * Boundary:
 * 说明当前类或方法的职责边界。
 */

/*
 * Deferred:
 * 说明当前阶段故意暂缓的能力、暂缓原因和后续阶段。
 */

/*
 * Risk:
 * 说明当前实现的风险、影响范围和后续处理方向。
 */
```

重点区域包括 security、authz、datascope、audit、dynamic-route、Keycloak mapping、permission code enforce、`CurrentUser`、`SecurityConfig`、`GlobalExceptionHandler`。

## 检查脚本

新增 `scripts/check-comments.sh`，提供启发式强制检查：

- Java 核心类缺少类头 Javadoc。
- public Controller/Service/Provider/AuthZ/Audit/DataScope 方法缺少 Javadoc。
- framework 关键类缺少 Boundary / Deferred / Risk 说明。
- 孤立 TODO。
- 明显低价值中文注释。

脚本必须可在本地执行，失败时输出文件路径和原因，不误伤 DTO/VO 简单字段类，不要求 getter/setter 注释。

## 不做事项

本阶段禁止：

- 修改业务逻辑。
- 修改 SQL。
- 修改 API 路径。
- 修改权限逻辑。
- 修改返回结构。
- 重构类名或包名。
- 大规模格式化无关文件。
- 提交 `target/`、`dist/`、`node_modules/`。
- 自动 git commit。

## 验收标准

- 已创建 `specs/006-code-comment-standardization/` 四件套。
- 已完成 006A、006B、006C 范围内重点注释标准化。
- 已新增 `scripts/check-comments.sh`。
- 已将 `scripts/check-comments.sh` 纳入 governance 检查清单。
- 验证命令按本阶段要求执行并记录结果。
- 最终 diff 不包含业务逻辑、SQL、API、权限逻辑或构建产物变更。

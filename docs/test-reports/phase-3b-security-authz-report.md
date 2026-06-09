# Phase 3B Security & AuthZ Hardening 测试报告

## 测试时间

2026-06-08 22:20-22:35 +0800

## 当前分支

`codex/phase-3a-system-management`

## 当前 commit

`6ae079d5859e0a192faeb4ab76d5a5d0d3cc0dda feat(system): implement core management baseline`

## 测试环境

- OS：WSL + Windows 宿主机 Docker
- Java：OpenJDK 21.0.11
- Maven：Apache Maven 3.9.9
- Node：v23.11.1
- pnpm：9.15.3
- PostgreSQL：Docker 容器 `platform-postgres`
- Keycloak：Docker 容器 `platform-keycloak`，本轮 smoke 使用 `platform` realm 的 `admin-cli` 获取真实 JWT
- Spring Boot 启动端口：`8081`
- Flyway 回归库：`platform_core_phase3b_regression`

## 完成内容

- 新增 Phase 3B Spec 四件套：`spec.md`、`plan.md`、`tasks.md`、`progress.md`。
- 收敛 Spring Security 主链：
  - `/api/system/**` 默认需要认证。
  - `/actuator/health` 允许匿名访问。
  - 其他 actuator endpoint 需要认证。
- 实现统一 JSON 错误响应：
  - 未登录：401。
  - 无权限：403。
  - 参数校验失败：400。
  - 业务异常 / 未知异常：统一 `code/message/data`。
- 接入 Keycloak JWT `sub` 到 `sys_user.keycloak_user_id` 的业务用户映射。
- 实现 `CurrentUserProvider`、`PermissionService`、`@RequiresPermission` 拦截。
- 新增系统认证授权接口：
  - `/api/system/auth/profile`
  - `/api/system/auth/routes`
  - `/api/system/auth/permissions`
- 实现 data scope 基础契约和系统侧条件提供器。
- 实现 `sys_oper_log` 操作审计落库。
- 修复用户分页和岗位分页 `total=0`。
- 前端接入真实权限接口，支持登录后加载 profile/routes/permissions、动态路由注入和按钮权限判断。

## 修改文件列表

- `platform-core/platform-admin/pom.xml`
- `platform-core/platform-admin/src/main/java/com/example/platform/admin/PlatformAdminApplication.java`
- `platform-core/platform-common/src/main/java/com/platform/core/common/api/ApiResult.java`
- `platform-core/platform-framework/pom.xml`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/audit/AuditLogAspect.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/config/SecurityConfig.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/authz/RequiresPermissionAspect.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/context/CurrentUser.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/context/CurrentUserProvider.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/web/RestAccessDeniedHandler.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/web/RestAuthenticationEntryPoint.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/web/SecurityJsonResponseWriter.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/web/GlobalExceptionHandler.java`
- `platform-core/platform-framework/src/test/java/com/platform/core/framework/security/authz/AuthorizationFoundationTest.java`
- `platform-core/platform-system/pom.xml`
- `platform-core/platform-system/src/main/java/com/platform/core/system/audit/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/auth/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/dept/controller/SysDeptController.java`
- `platform-core/platform-system/src/main/java/com/platform/core/system/menu/controller/SysMenuController.java`
- `platform-core/platform-system/src/main/java/com/platform/core/system/post/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/role/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/user/**`
- `platform-core/platform-system/src/main/resources/mapper/system/**`
- `platform-ui/src/api/system/auth.ts`
- `platform-ui/src/layouts/MainLayout.vue`
- `platform-ui/src/permissions/index.ts`
- `platform-ui/src/router/index.ts`
- `platform-ui/src/store/user.ts`
- `specs/005-security-authz-hardening/**`

## 验证命令

后端：

```bash
cd platform-core
mvn spotless:check checkstyle:check test
mvn package -DskipTests
```

前端：

```bash
cd platform-ui
pnpm lint
pnpm build
```

文档与格式：

```bash
scripts/check-spec-progress.sh
git diff --check
```

运行时 smoke：

```bash
java -jar platform-admin/target/platform-admin-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=dev \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/platform_core_phase3b_regression
```

## API 测试结果

- Flyway 回归库 `platform_core_phase3b_regression` 启动成功，`flyway_schema_history` 最新版本为 `2` 且 `success=true`。
- `/actuator/health` 未登录访问返回 HTTP 200，响应 `{"status":"UP"}`。
- `/actuator/info` 未登录访问返回 HTTP 401，响应 `{"code":401,"message":"unauthorized","data":null}`。
- `/api/system/users` 未登录访问返回 HTTP 401，响应 `{"code":401,"message":"unauthorized","data":null}`。
- 临时移除测试库管理员角色权限后访问 `/api/system/users` 返回 HTTP 403，响应 `{"code":403,"message":"forbidden","data":null}`；测试后已恢复角色权限。
- 使用 Keycloak `platform` realm 的真实 JWT 访问 `/api/system/auth/profile` 返回 HTTP 200，响应包含 `userId`、`roleIds`、`roleKeys`、`permissions`、`dataScope`。
- 使用 Keycloak JWT 访问 `/api/system/auth/routes` 返回 HTTP 200，响应为菜单路由树。
- 使用 Keycloak JWT 访问 `/api/system/auth/permissions` 返回 HTTP 200，超级管理员返回 `*:*:*`。
- 使用 Keycloak JWT 访问 `/api/system/users` 返回 HTTP 200，分页 `total=1`。
- 使用 Keycloak JWT 访问 `/api/system/posts` 返回 HTTP 200，分页 `total` 不再为 0。
- 使用 Keycloak JWT 访问 `/api/system/users?pageNum=0&pageSize=-1` 返回 HTTP 400，响应 `{"code":400,"message":"bad request","data":null}`。
- 使用 Keycloak JWT 创建岗位返回 HTTP 200，随后 `sys_oper_log` 计数为 `1`，确认操作审计落库。

## 已修复问题

- 修复 `/api/system/**` 未登录可访问问题。
- 修复 `/actuator/health` 未登录返回 401 问题。
- 修复参数校验异常变成 401 空响应问题。
- 修复无权限响应不统一问题。
- 修复用户分页 `total=0` 问题。
- 修复岗位分页 `total=0` 问题。
- 补齐 Keycloak JWT `sub` 到业务用户的映射链路。
- 补齐 `permission_code` 后端 enforce。
- 补齐动态路由和权限列表接口。
- 补齐操作审计落库。

## 仍 Deferred 事项

- data scope 当前完成基础契约和条件提供器，尚未全局注入全部业务 SQL。后续需要补充 AOP / SQL 条件拼接覆盖面，并为 `ALL`、`CUSTOM`、`DEPT`、`DEPT_AND_CHILD`、`SELF` 建立组合测试。
- 本地 Keycloak `platform-client` 应用配置与容器中的实际 client secret / direct grant 设置不一致。本轮 smoke 使用同 realm 的 `admin-cli` 获取真实 JWT，未绕过生产安全逻辑，但仍需要统一开发环境配置。
- 默认管理员种子 `admin-keycloak-sub` 仍是部署前占位符。本轮仅在临时回归库中改为真实 Keycloak subject 做 smoke。
- 登录日志 `sys_login_log` 未在本阶段落库。
- 前端权限不足状态已有基础处理，仍需要后续 UI 验收细化 403 页面与交互。
- Vite 构建存在大 chunk warning，后续前端性能治理阶段再拆包。

## 风险与技术债

- `super_admin` 当前通过 `*:*:*` 拥有全部权限，后续必须补充非超级管理员、无权限用户、自定义数据权限的真实集成测试。
- data scope 如果不进入 SQL 查询层，会形成“接口存在但权限不生效”的风险。
- Keycloak client 配置不统一会影响真实登录 e2e 和开发复现。
- `target/`、`dist/` 是构建产物，不应提交。
- `docs/prd-generation-context.md` 是上下文辅助文件，不应提交到正式项目。

## 验证结论

- `mvn spotless:check checkstyle:check test`：通过。
- `mvn package -DskipTests`：通过。
- `pnpm lint`：通过。
- `pnpm build`：通过，存在非阻塞 chunk size warning。
- `scripts/check-spec-progress.sh`：通过。
- `git diff --check`：通过。
- Phase 3B 必须修复项均已通过本轮 smoke 或构建验证。

## 是否建议进入下一阶段

建议先进行一次人工审查和提交范围清理，再进入下一阶段。

下一阶段不建议继续扩大业务模块范围。优先级应为：

1. 补齐 data scope 在核心列表查询中的真实 SQL 生效。
2. 统一 Keycloak 开发环境 client 配置。
3. 增加非超级管理员、无权限用户、自定义部门权限的集成测试。
4. 清理构建产物和不应提交文件，确保提交范围只包含源码、Spec 和测试报告。

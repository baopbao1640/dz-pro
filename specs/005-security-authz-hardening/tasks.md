# Phase 3B Security & AuthZ Hardening Tasks

## Step 0：Spec 冻结

- [x] 创建 `specs/005-security-authz-hardening/spec.md`。
- [x] 创建 `specs/005-security-authz-hardening/plan.md`。
- [x] 创建 `specs/005-security-authz-hardening/tasks.md`。
- [x] 创建 `specs/005-security-authz-hardening/progress.md`。
- [x] 明确串行主链与并行旁路边界。

## Step 1：agent-security-main

- [x] 实现统一 JSON 错误响应结构。
- [x] 收敛 `SecurityFilterChain`。
- [x] `/api/system/**` 默认认证。
- [x] `/actuator/health` 匿名访问。
- [x] 其他 actuator endpoint 保护。
- [x] 未登录返回 401 JSON。
- [x] 无权限返回 403 JSON。
- [x] 参数校验失败返回 400 JSON。
- [x] 业务异常和未知异常返回 JSON。
- [x] JWT `sub` 映射 `sys_user.keycloak_user_id`。
- [x] 禁用或删除用户拒绝访问。
- [x] 实现 `CurrentUserProvider`。
- [x] 实现 permission code 查询。
- [x] 实现 `@RequiresPermission` enforce。
- [x] 实现 data scope 基础契约。
- [x] 实现 `/api/system/auth/profile`。
- [x] 实现 `/api/system/auth/routes`。
- [x] 实现 `/api/system/auth/permissions`。

## Step 2：旁路 Agent

### agent-audit

- [x] 实现 system 侧 `AuditEventHandler`。
- [x] 写入 `sys_oper_log`。
- [x] 脱敏参数和结果。
- [x] 异步失败不影响主流程。

### agent-frontend-auth

- [x] 增加前端 auth API client。
- [x] 登录后加载 profile/routes/permissions。
- [x] 动态 route inject。
- [x] 按钮权限判断。
- [x] 权限不足状态。

### agent-pagination-fix

- [x] 修复用户分页 total。
- [x] 修复岗位分页 total。
- [x] 增加分页测试。

## Step 3：联调与报告

- [x] 后端 `mvn spotless:check checkstyle:check test`。
- [x] 后端 `mvn package -DskipTests`。
- [x] 前端 `pnpm lint`。
- [x] 前端 `pnpm build`。
- [x] `scripts/check-spec-progress.sh`。
- [x] `git diff --check`。
- [x] 生成 `docs/test-reports/phase-3b-security-authz-report.md`。
- [x] 更新 `progress.md`。

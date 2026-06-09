# Phase 3B Security & AuthZ Hardening Plan

## 当前策略

采用“串行主链 + 并行旁路”：

- 安全主链只由 `agent-security-main` 串行修改。
- audit、frontend auth、pagination、test report 可以在主链契约明确后并行推进。
- 所有旁路 Agent 不得修改 `SecurityFilterChain`、JWT 解析、`CurrentUser` 解析、`@RequiresPermission` enforce、data scope SQL 主逻辑。

## Step 0：Spec 冻结

已创建 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。

必须基于以下文档：

- `specs/000-documentation-governance/`
- `specs/001-system-database-design/`
- `specs/002-system-user-role-menu-org/`
- `specs/004-code-comment-governance/`
- `docs/test-reports/phase-3a-full-regression-report.md`

## Step 1：agent-security-main 串行主链

实现顺序：

1. 统一错误响应基础结构。
2. 收敛 `SecurityFilterChain`。
3. 配置 401 / 403 JSON handler。
4. 配置参数校验、业务异常和未知异常 JSON。
5. 实现 JWT `sub` 到 `sys_user` 映射。
6. 实现 `CurrentUserProvider`。
7. 实现 `PermissionService` / permission checker。
8. 让 `@RequiresPermission` 生效。
9. 实现 data scope 基础契约。
10. 提供 `/api/system/auth/profile`、`/api/system/auth/routes`、`/api/system/auth/permissions`。

关键决策：

- `/actuator/health` permitAll。
- `/api/system/**` authenticated。
- 认证失败不进入业务 Controller。
- framework 不依赖 system；system 通过实现 framework 契约接入。
- 缺少本地用户映射时返回 403 或业务化 403 JSON，不自动创建用户。

## Step 2：旁路 Agent 并行

### agent-audit

- system 模块实现 `AuditEventHandler`。
- 写入 `sys_oper_log`。
- 失败只记录日志，不阻塞主流程。
- 使用 `CurrentUserProvider` 契约获取当前用户，不能修改上下文解析。

### agent-frontend-auth

- 增加 auth API client。
- 前端登录后拉取 profile/routes/permissions。
- 动态 route inject。
- 按钮 permission code 判断。
- 权限不足页面状态。

### agent-pagination-fix

- 修复用户分页 total。
- 修复岗位分页 total。
- 增加分页测试。

### agent-test-report

- 只做测试和报告。
- 不修业务逻辑。

## Step 3：联调

验证：

- 安全链。
- JWT 映射。
- 权限码。
- 数据权限。
- 动态路由。
- 分页 total。
- 审计事件。
- 前端 lint/build。

## 不做事项

- 不做本地登录。
- 不做 Spring Cloud。
- 不做 Dict / Notice / Quartz / Monitor / Online User。
- 不做 UI 精修。
- 不做多租户。
- 不提交构建产物。

## 验证命令

```bash
cd platform-core
mvn spotless:check checkstyle:check test
mvn package -DskipTests

cd ../platform-ui
pnpm lint
pnpm build

cd ..
scripts/check-spec-progress.sh
git diff --check
```

## 报告

最终生成：

```text
docs/test-reports/phase-3b-security-authz-report.md
```

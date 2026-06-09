# Phase 3B Security & AuthZ Hardening Spec

## 目标

在 Phase 3A 基础系统管理能力可运行的基础上，收紧安全链、统一错误响应、接入 Keycloak JWT 到本地 `sys_user` 的业务用户映射，并让权限码、数据权限、动态路由、审计落库进入可验证状态。

本阶段重点是安全和授权硬化，不扩展新的业务模块。

## 背景

Phase 3A 回归报告确认系统管理核心能力可以启动、迁移、构建和冒烟访问，但存在以下必须处理的问题：

- `/api/system/**` 未登录可访问。
- 参数校验异常最终表现为 HTTP 401 且响应体为空。
- `/actuator/health` 未登录返回 401，安全策略不清晰。
- 用户和岗位分页接口 `records` 有数据但 `total=0`。
- permission code、data scope、dynamic route、audit persistence 仍处于 scaffold 或 Deferred。

## 范围

本阶段包含：

- SecurityFilterChain 收敛。
- 401 / 403 / 400 / 500 统一 JSON 响应。
- Keycloak JWT `sub` 到 `sys_user.keycloak_user_id` 映射。
- 当前用户上下文 `CurrentUser` 落地。
- permission code enforce。
- `@RequiresPermission` 生效。
- data scope 基础实现与契约验证。
- audit event 落库到 `sys_oper_log`。
- 前端真实权限接口适配、动态路由注入和按钮权限判断。
- 用户、岗位分页 `total` 修复。
- Phase 3B 测试报告。

## 不做事项

本阶段禁止：

- 引入 Spring Cloud。
- 绕过 Keycloak 或实现本地密码登录。
- 全局 `permitAll`。
- 让 `platform-framework` 依赖 `platform-system`。
- 扩展 Dict、Notice、Quartz、Monitor、Online User 等新业务域。
- 提交 `target/`、`dist/`、`node_modules/` 或临时上下文文件。
- 多个 Agent 同时修改安全主链。
- 自动 git commit。

## 安全主链规则

`agent-security-main` 是唯一允许修改安全主链核心文件和逻辑的执行者。

必须实现：

- `/api/system/**` 默认需要认证。
- `/actuator/health` 允许匿名访问。
- 其他 actuator endpoint 继续受保护。
- 未登录返回 401 JSON。
- 已登录但无权限返回 403 JSON。
- 参数校验失败返回 400 JSON。
- 业务异常和未知异常返回统一 JSON。
- JWT `sub` 映射本地 `sys_user`。
- 本地用户不存在、删除或禁用时拒绝访问。
- permission code 从本地角色菜单关系计算。
- `@RequiresPermission` 基于当前用户权限码校验。

## Keycloak 边界

Keycloak 仍负责认证、密码、session、token、MFA 和身份生命周期。

平台只使用 JWT `sub` 作为外部身份标识，并映射到 `sys_user.keycloak_user_id`。平台不得保存密码、refresh token、session 或 MFA 信息。

若真实 Keycloak JWT e2e 条件不足，必须通过单元测试或集成测试覆盖契约，不得写生产绕过逻辑。

## API 契约

新增或硬化系统认证授权接口：

| Method | Path | 说明 |
| --- | --- | --- |
| `GET` | `/api/system/auth/profile` | 当前用户业务身份、角色、权限和数据范围摘要。 |
| `GET` | `/api/system/auth/routes` | 当前用户可访问动态路由树。 |
| `GET` | `/api/system/auth/permissions` | 当前用户 permission code 列表。 |

所有接口必须使用统一响应：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

错误响应必须保持 JSON：

```json
{
  "code": 401,
  "message": "unauthorized",
  "data": null
}
```

## 数据权限

本阶段至少实现可验证基础能力：

- `ALL`
- `CUSTOM`
- `DEPT`
- `DEPT_AND_CHILD`
- `SELF`

data scope 可以先以服务层查询条件或 mapper 参数方式落地，但必须保留 AOP / SQL 条件拼接扩展边界。

## 审计落库

审计事件必须异步写入 `sys_oper_log`。

要求：

- 敏感字段脱敏。
- 写入失败不阻塞主流程。
- `platform-framework` 只发布事件和定义 handler 契约。
- `platform-system` 提供真实 handler。
- 不修改 SecurityFilterChain、CurrentUser 解析或 permission enforce 逻辑。

## 前端权限

前端必须通过真实后端接口获取：

- 当前用户 profile。
- 当前用户 routes。
- 当前用户 permissions。

前端不得硬编码管理员权限，不得自行伪造权限。

## 分页修复

必须修复：

- 用户分页 `total=0`。
- 岗位分页 `total=0`。

分页响应继续保持：

```json
{
  "records": [],
  "total": 0,
  "pageNum": 1,
  "pageSize": 10
}
```

## 验收标准

- 未登录访问 `/api/system/**` 返回 401 JSON。
- `/actuator/health` 返回 200。
- 参数校验失败返回 400 JSON。
- 无权限访问返回 403 JSON。
- 已登录用户可通过 JWT `sub` 映射到本地 `sys_user`。
- 禁用用户被拒绝访问。
- `@RequiresPermission` 生效。
- 用户和岗位分页 total 正确。
- 审计事件可落库或明确 Deferred 原因。
- 前端 lint/build 通过。
- 后端 Spotless、Checkstyle、test、package 通过。
- 生成 `docs/test-reports/phase-3b-security-authz-report.md`。

# 当前阶段

Phase 3B - Security & AuthZ Hardening 已完成本轮实现与回归验证，当前停止在 Phase 3B 收尾状态，等待确认是否进入下一阶段。

# 当前目标

修复 Phase 3A 回归报告暴露的安全链和授权问题：收紧 `/api/system/**`、统一 JSON 错误响应、接入 Keycloak JWT 到本地用户映射、启用 permission code 校验、落地 data scope 基础能力、修复分页 total，并补齐审计落库和前端权限联动。

# 当前完成情况

- 已读取 Documentation Governance、数据库设计、系统管理 Feature Spec、代码注释治理规范和 Phase 3A 回归报告。
- 已创建 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。
- 已按“串行主链 + 并行旁路”完成安全主链和旁路任务。
- 已收敛 `SecurityFilterChain`：`/api/system/**` 默认认证，`/actuator/health` 匿名访问，其他 actuator endpoint 受保护。
- 已实现 401 / 403 / 400 / 500 统一 JSON 响应结构。
- 已实现 Keycloak JWT `sub` 到 `sys_user.keycloak_user_id` 的业务用户映射。
- 已实现 `CurrentUserProvider`、permission code 查询、`@RequiresPermission` enforce。
- 已实现 data scope 基础契约和系统侧条件提供器，但尚未覆盖所有业务 SQL 的全局注入。
- 已新增 `/api/system/auth/profile`、`/api/system/auth/routes`、`/api/system/auth/permissions`。
- 已实现 `sys_oper_log` 审计落库，审计写入异步执行且失败不影响主流程。
- 已修复用户分页和岗位分页 `total=0` 问题。
- 已完成前端 auth API client、登录后权限加载、动态 route inject 和按钮权限判断。
- 已生成 `docs/test-reports/phase-3b-security-authz-report.md`。

# 当前架构决策

- 安全主链必须串行推进。原因是 `SecurityFilterChain`、JWT 解析、当前用户上下文、permission enforce 和 data scope 之间强耦合，并行修改会造成权限上下文错乱。
- `platform-framework` 只定义安全、授权、审计和数据权限契约，不依赖 `platform-system`。原因是保持 Modular Monolith 内部模块边界，避免 framework 反向绑定业务表。
- Keycloak 仍是唯一认证源。平台只消费 JWT `sub` 并映射 `sys_user.keycloak_user_id`，不引入本地密码、session 或 token 存储。
- `/api/system/**` 已从 Phase 3A 临时放行收敛为默认认证。原因是系统管理接口已经具备核心业务能力，继续匿名访问不可接受。
- `/actuator/health` 允许匿名访问，其他 actuator endpoint 继续保护。原因是健康探活与运维监控需要最小公开面。
- 审计落库通过 framework 事件与 system handler 解耦。原因是 framework 不能依赖业务模块，审计失败也不能阻塞主流程。

# 已实现能力

- Phase 3B Spec 四件套。
- 统一 API 成功/错误响应结构。
- Spring Security resource server 保护系统管理接口。
- Keycloak JWT `sub` 到本地业务用户映射。
- 当前用户上下文、权限码查询、权限注解拦截。
- 动态路由和权限列表接口。
- data scope 基础契约。
- 审计事件异步落库到 `sys_oper_log`。
- 用户和岗位分页 total 修复。
- 前端权限加载、动态路由注入、按钮权限判断。
- Phase 3B 回归测试报告。

# Deferred（暂缓事项）

- data scope 目前是基础契约和条件提供器，尚未全局注入所有 Mapper SQL；后续需要扩展 AOP / SQL 条件拼接覆盖面。
- 本地 Keycloak `platform-client` 配置与实际容器 client secret / direct grant 设置不一致，本轮真实 JWT smoke 使用同 realm 的 `admin-cli` 获取 token；后续需要统一开发环境 Keycloak client 配置。
- 默认管理员种子 `admin-keycloak-sub` 仍是部署前占位符；真实环境必须替换或通过用户同步流程绑定。
- 前端权限不足页面状态已有基础处理，仍需要在后续 UI 验收中细化 403 页面和交互提示。
- 登录日志 `sys_login_log` 未在本阶段落库；本阶段重点完成操作审计 `sys_oper_log`。

# 风险与技术债

- 风险：data scope 如果只停留在条件模型，无法真正限制所有业务查询结果。后续 Phase 3C/权限专项必须补齐 SQL 应用点和测试覆盖。
- 风险：Keycloak client 配置不一致会影响真实登录链路。需要在环境治理阶段统一容器配置、应用配置和开发文档。
- 风险：`super_admin` 当前通过 `*:*:*` 放行全部权限，后续需要补充非超级管理员的真实角色、菜单、部门数据权限组合测试。
- 风险：Vite 生产构建存在大 chunk 警告，当前不阻塞 Phase 3B，但后续前端性能治理需要拆包。
- 技术债：`target/` 和 `dist/` 都是构建产物，不应进入提交；`docs/prd-generation-context.md` 仍是不应提交的上下文文件。

# 验证结果

- `cd platform-core && mvn spotless:check checkstyle:check test`：通过。
- `cd platform-core && mvn package -DskipTests`：通过。
- `cd platform-ui && pnpm lint`：通过。
- `cd platform-ui && pnpm build`：通过，存在 chunk size warning。
- 干净 PostgreSQL 回归库执行 Flyway V1 + V2：通过，schema version 为 `2`。
- `/actuator/health` 未登录访问：HTTP 200，响应 `{"status":"UP"}`。
- `/actuator/info` 未登录访问：HTTP 401，统一 JSON。
- `/api/system/users` 未登录访问：HTTP 401，统一 JSON。
- 临时移除角色权限后访问 `/api/system/users`：HTTP 403，统一 JSON。
- 带真实 Keycloak realm JWT 访问 `/api/system/auth/profile`、`/api/system/auth/routes`、`/api/system/auth/permissions`：HTTP 200。
- 带 JWT 访问 `/api/system/users` 和 `/api/system/posts`：HTTP 200，分页 `total` 正常。
- 带 JWT 访问非法分页参数：HTTP 400，统一 JSON。
- 带 JWT 创建岗位触发审计：HTTP 200，`sys_oper_log` 计数增加到 1。
- `scripts/check-spec-progress.sh`：通过。
- `git diff --check`：通过。

# 下一阶段计划

- 不建议继续扩大业务 CRUD 范围，先确认 Phase 3B 的安全行为与报告。
- 下一阶段应优先补齐 data scope 在核心列表查询中的真实 SQL 生效和测试覆盖。
- 统一 Keycloak 开发环境 client 配置，避免真实登录链路继续依赖临时 smoke 方式。
- 补充非超级管理员角色、菜单、部门数据权限的组合测试。
- 清理构建产物提交范围，确保 `target/`、`dist/`、`docs/prd-generation-context.md` 不进入提交。

# Agent 协作备注

`agent-security-main` 已完成安全主链核心文件和逻辑。本阶段没有采用多个 Agent 同时修改 `SecurityConfig`、`SecurityFilterChain`、`CurrentUser` 解析、`@RequiresPermission` enforce、data scope SQL 逻辑或 JWT `sub` 解析逻辑。

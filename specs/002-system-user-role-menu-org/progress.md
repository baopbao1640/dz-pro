# 当前阶段

Phase 2A Feature Spec 已冻结；Phase 3A 整体测试与回归验证已完成，基础系统管理核心能力可启动、可迁移、可构建，但仍有安全链、统一异常、分页 total、数据权限和审计落库风险需要进入 Phase 3B 优先处理。

# 当前目标

为用户、角色、菜单、部门、岗位建立系统管理核心能力，并保持 Modular Monolith、Keycloak 认证边界、Flyway-only schema、MyBatis Plus、PostgreSQL、Ant Design Vue 和不引入 Spring Cloud 的约束。

# 当前完成情况

- 已完成 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。
- 已完成 Phase 2A 冻结设计，覆盖用户、角色、菜单、部门、岗位。
- 已定义 API、DTO、VO、权限点、页面结构、搜索条件、表格字段、表单校验、状态流转、审计日志、数据权限和前后端模块边界。
- 已提交 Phase 2A 设计：`200b7aa docs(system): add user role menu org feature spec`。
- 已创建 Phase 3A 分支：`codex/phase-3a-system-management`。
- 已接入 `platform-common` 通用响应和分页结构。
- 已建立 `platform-framework` 权限、数据权限、动态路由和异步审计基础骨架。
- 已建立 `platform-system` 用户、部门、岗位、角色、菜单的基础后端结构。
- 已建立系统管理前端导航和用户、部门、岗位、角色、菜单页面。
- 已完成后端启动集成：PostgreSQL 连接、Flyway v2 校验、系统 API smoke。
- 已完成 Phase 3A 整体测试与回归验证报告：`docs/test-reports/phase-3a-full-regression-report.md`。

# 当前架构决策

- 先完成用户、部门、岗位，再推进角色、菜单、权限和动态路由。原因是用户归属、部门树和岗位关系是角色与数据权限的基础依赖。
- 认证仍由 Keycloak 负责，`sys_user` 只作为业务用户映射。原因是避免本地认证体系与 Keycloak 边界冲突。
- 权限和数据权限先提供注解、上下文和条件构造骨架，不立即全局强制。原因是权限来源、菜单授权和用户映射仍需在后续阶段统一收敛。
- 审计先实现异步发布与脱敏骨架，暂不直接持久化。原因是 framework 不应直接依赖 system 日志表，避免跨模块反向依赖。
- 前端统一使用 Ant Design Vue 成熟组件。原因是系统管理页面应保持稳定、可扫描、可维护，不自定义替代通用组件。
- 角色/菜单页面先做到基础可用，不做 UI 精修。原因是当前阶段目标是核心能力可运行，不是交互细节打磨。

# 已实现能力

- 用户：分页、详情、新增、编辑、启用/禁用、分配角色、分配岗位的基础后端接口与前端入口。
- 部门：树查询、新增、编辑、状态控制、排序、删除限制的基础后端接口与前端页面。
- 岗位：分页、详情、新增、编辑、状态控制、删除限制的基础后端接口与前端页面。
- 角色：列表、详情、新增、编辑、状态控制、菜单分配、数据权限更新的基础后端接口；前端基础表格与表单。
- 菜单：树查询、详情、新增、编辑、状态控制、删除限制、动态路由和权限码查询的基础后端接口；前端基础树表与表单。
- 权限基础：`RequiresPermission`、`DataPermission`、`DataScope`、当前用户上下文、动态路由 DTO 骨架。
- 审计基础：操作类型枚举、审计 annotation、事件模型、脱敏、异步发布器和 no-op fallback handler。
- 前端状态：loading、empty、disabled、error、permission denied 基础状态。

# Deferred（暂缓事项）

- 动态路由真实权限联动：当前已有 API 和前端注入骨架，但尚未按当前 Keycloak 用户真实角色加载。
- 数据权限 SQL 注入器：当前只有条件表达和注解基础，尚未全局拼接 SQL。
- audit persistence：当前异步审计 handler 仍是 no-op fallback，尚未写入 `sys_oper_log` 或 `sys_login_log`。
- permission cache：暂未实现权限缓存、失效和刷新策略。
- 角色菜单半选持久化细节：已有接口基础，后续需要结合前端 tree half-check 行为补强。
- 用户与 Keycloak 生命周期同步：当前只维护本地业务映射，暂未实现 Keycloak provisioning 或同步任务。
- OpenAPI 注解覆盖：基础接口已存在，但注解完善应在接口稳定后处理。
- UI 精修、国际化、多租户和性能极限优化均不属于当前阶段。

# 风险与技术债

- 风险：`/actuator/health` 当前仍存在安全链暴露问题。影响范围是运维探活。处理方向是单独收敛 SecurityFilterChain matcher 和 actuator 安全策略。
- 风险：系统 API smoke 在未登录情况下临时放行 `/api/system/**`。影响范围是权限安全。处理方向是接入真实当前用户、权限码校验和按钮级控制后取消临时放行。
- 风险：参数校验异常当前最终表现为 HTTP 401 且响应体为空。影响范围是前端错误提示和 API 契约稳定性。处理方向是 Phase 3B 增加统一异常处理，并避免 `/error` 或错误响应路径被安全链误拦截。
- 风险：用户、岗位分页接口返回 records 有数据但 `total=0`。影响范围是前端分页器和列表体验。处理方向是 Phase 3B 修正 MyBatis Plus 分页统计或自定义分页总数查询。
- 风险：审计未持久化。影响范围是操作追踪与合规。处理方向是在 system 模块实现日志表写入 handler，并保持异步非阻塞。
- 风险：数据权限尚未真正作用于查询。影响范围是部门隔离和角色数据范围。处理方向是实现 AOP 或 MyBatis 拦截式 SQL 条件拼接。
- 技术债：当前存在 `com.example.platform.admin` 与 `com.platform.core` 包名混用。当前接受原因是避免无关重构。后续应在单独技术债任务中收敛命名。
- 技术债：部分 CRUD 仍是基础可运行版本，负向测试、边界校验和异常响应需要补强。
- 技术债：前端 permission code 当前仍偏 scaffold，需要与后端当前用户权限接口对齐。

# 下一阶段计划

- Phase 3A 回归完成后可以进入 Phase 3B，但 Phase 3B 首批任务必须优先处理安全链、统一异常响应、分页 total、真实权限校验、数据权限落地和审计持久化。
- 不建议在处理上述 P0/P1 风险前继续扩大业务 CRUD 范围。
- 每个后续子阶段必须按模块拆分，做到一个模块、编译、测试、修复，再继续。
- 继续推进前必须基于本文件确认 Deferred、风险和技术债，而不是读取旧流水日志。

# 验证结果

- Phase 2A 文档检查已通过：目录只包含 `spec.md`、`plan.md`、`tasks.md`、`progress.md`，无 Java/Vue/XML/OpenAPI 实现。
- 后端检查已通过：`cd platform-core && mvn spotless:check checkstyle:check test`。
- 后端打包已通过：`cd platform-core && mvn package -DskipTests`。
- 前端检查已通过：`cd platform-ui && pnpm lint`。
- 前端构建已通过：`cd platform-ui && pnpm build`。
- Spring Boot jar 已启动成功，PostgreSQL 与 Flyway v2 校验正常。
- API smoke 已确认 `/api/system/depts/tree`、`/api/system/posts`、`/api/system/roles`、`/api/system/menus/tree` 返回 `code:200`。
- Phase 3A 整体回归验证已通过后端完整命令序列：`mvn clean`、`mvn spotless:check`、`mvn checkstyle:check`、`mvn test`、`mvn package -DskipTests`。
- 干净 PostgreSQL 测试库 `platform_core_phase3a_regression` 已确认 Flyway V1 + V2 执行成功，schema version 达到 `v2`。
- 数据库断言已确认：17 张 `sys_*` 表、9 个 partial unique index、5 个基础字典类型、默认管理员映射 `admin-keycloak-sub`、`sys.log.retention.days = 180`。
- API smoke 已补充确认 `/api/system/users`、`/api/system/menus/tree` 返回 HTTP 200 与统一 `code/message/data` 结构。
- 安全链当前行为已记录：未登录访问 `/api/system/**` 当前临时放行；未登录访问 `/actuator/health` 返回 401；未登录访问 `/api/user/info` 返回 401。
- 已发现并记录分页 `total=0` 与错误响应 401 空响应问题，未在本阶段修复。
- 前端 `pnpm lint` 与 `pnpm build` 已通过，构建产生的 `platform-ui/dist/` 不应提交。
- 文档与格式检查已通过：`scripts/check-spec-progress.sh`、`git diff --check`。`git diff --check` 仅输出 LF/CRLF 提示，无 whitespace error。
- P0 注释治理抽查已通过：`SecurityConfig`、audit、authz、datascope、dynamic-route 存在中文 Boundary / Deferred / Risk 注释，未发现孤立 `TODO`。

# Agent 协作备注

多 Agent 协作只保留职责和边界结论，不再记录执行流水。后续 Agent 不得覆盖共享结构；如需修改通用 Result、Security Context、Permission annotation、DTO/VO 公共结构，必须先更新本文件中的当前架构决策、风险和下一阶段计划。

# 当前阶段

P0 注释重构阶段：framework 下 security、authz、audit、datascope、dynamic-route 与 `SecurityConfig` 注释治理已完成。

# 当前目标

在不修改业务逻辑、不移动文件、不调整方法签名的前提下，为 P0 framework 代码补充中文边界注释，明确 Keycloak 边界、framework 边界、scaffold / Deferred、风险和后续扩展点。

# 当前完成情况

- 已创建 `specs/004-code-comment-governance/` 四件套。
- 已定义 Java 类注释和方法注释规范。
- 已定义 framework、security、authz、audit、datascope、dynamic-route 重点注释规范。
- 已定义 Deferred、风险/限制说明、MyBatis XML、Vue、TypeScript API client 注释规范。
- 已定义 TODO 替代规范和无意义注释禁用规则。
- 已列出优先重构模块、不建议立即重构模块和后续实施顺序。
- 已完成 P0 注释试点，范围限定在 `platform-framework` 下 security、authz、audit、datascope、dynamic-route 与 `SecurityConfig`。
- 已将英文复述型注释替换为中文边界说明。
- 已补充审计 no-op fallback、权限数据来源、数据权限 SQL 条件、动态路由菜单关系和临时安全放行的 Deferred / Risk 说明。

# 当前架构决策

- 先做规范，不直接改代码：当前工作区已有 Phase 3A 代码改动，直接大规模改注释会增加冲突和 review 噪音。
- framework 相关代码优先治理：这些模块决定系统边界，注释质量会影响后续所有业务模块。
- Deferred 必须结构化：只写 `TODO` 无法表达暂缓原因、影响范围和后续处理方向。
- 简单 DTO、VO、getter、setter 不强制注释：避免为了注释而注释，降低代码噪音。
- P0 只改注释不改逻辑：当前工作区已有未提交业务代码，注释治理必须降低 review 噪音，不应混入行为变更。
- `SecurityConfig` 中 `/api/system/**` 临时放行只通过注释标记风险，不在本阶段调整安全逻辑。原因是用户明确要求本阶段只允许修改注释。

# 已实现能力

- 注释治理 Spec。
- 注释治理 Plan。
- 注释治理 Tasks。
- 符合 Documentation Governance 的进度记录。
- 后续注释重构优先级清单。
- P0 framework 注释治理试点。
- security/authz/audit/datascope/dynamic-route 关键边界注释。
- `SecurityConfig` 当前安全链风险注释。

# Deferred（暂缓事项）

- 暂不批量修改 Java/Vue/SQL/XML 代码注释。
- 暂不建立自动化注释 lint。
- 暂不接入 Checkstyle 或 ESLint。
- 暂不清理历史英文注释。
- 暂不要求所有 DTO、VO、Entity 立即补类注释。
- 暂不修复 `/api/system/**` 临时放行逻辑。
- 暂不实现 audit persistence。
- 暂不实现真实数据权限 SQL 注入器。
- 暂不实现当前用户动态路由真实权限联动。

# 风险与技术债

- 风险：后续如果一次性大规模补注释，可能产生大量无意义注释。处理方向是按 P0/P1/P2 分批试点。
- 风险：规范没有自动化门禁时，执行质量依赖 review。处理方向是后续增加轻量检查脚本。
- 技术债：现有 framework 和 system 代码中存在 scaffold、no-op、临时放行等边界，需要后续通过注释明确。当前接受原因是本阶段只做规范设计。
- 技术债：P0 已通过注释明确 scaffold 和 Deferred，但尚未偿还这些技术债。后续必须在独立实现阶段处理，不能把注释视为功能完成。
- 风险：注释与实现可能随代码演进失配。处理方向是后续修改安全、审计、数据权限、动态路由时同步更新对应注释和本 progress。

# 下一阶段计划

- 等用户确认后，进入 P1 注释治理：系统管理核心模块、MyBatis XML、前端 router/permissions。
- 在进入 P1 前，先确认 P0 注释没有引入逻辑 diff 和检查失败。
- 后续每批重构仍只改注释，不进入业务功能开发。

# 验证结果

- 已通过 `cd platform-core && mvn spotless:check checkstyle:check test`。
- 已通过 `scripts/check-spec-progress.sh`。
- 已通过 `git diff --check`；当前仅有既有 LF/CRLF 工作区提示，无 whitespace error。
- 已确认本阶段只调整 P0 范围内注释和本 `progress.md`，未修改方法签名、控制流、SQL、配置值或业务逻辑。

# Agent 协作备注

后续如多 Agent 分批治理注释，必须按模块拆分，不得跨模块批量扫改；每个 Agent 只允许补充边界、风险、Deferred 和扩展点注释，禁止添加复述型注释。

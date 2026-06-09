# Code Comment Governance Plan

# 当前阶段

当前只做注释治理规范设计，不进行代码注释批量重构。

# 实施边界

允许：

- 创建 `specs/004-code-comment-governance/` 四件套。
- 定义注释规范、优先级、Deferred、风险和实施顺序。
- 检查文档结构。

禁止：

- 修改 Java 代码。
- 修改 Vue 代码。
- 修改 TypeScript API client。
- 修改 MyBatis XML。
- 修改 SQL 或 Flyway。
- 进入业务功能开发。

# 设计策略

- 先建立规则，再做小批量试点。原因是当前代码已有多 Agent 生成痕迹，直接大规模改注释会放大冲突。
- 注释优先解释边界和风险。原因是 AI 生成代码最大移交问题不是语法可读性，而是维护者不知道哪些是已完成、哪些是 scaffold、哪些是 Deferred。
- framework 相关代码优先治理。原因是 framework、security、authz、audit、datascope、dynamic-route 会影响后续所有业务模块。
- 禁止孤立 TODO。原因是 TODO 无法表达阶段边界、风险影响和后续处理方向。

# 需要优先重构注释的模块清单

优先级 P0：

- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/audit/`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/config/SecurityConfig.java`
- `platform-core/platform-system/src/main/java/com/platform/core/system/menu/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/role/`

优先级 P1：

- `platform-core/platform-system/src/main/java/com/platform/core/system/user/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/dept/`
- `platform-core/platform-system/src/main/java/com/platform/core/system/post/`
- `platform-core/platform-system/src/main/resources/mapper/system/`
- `platform-ui/src/permissions/`
- `platform-ui/src/router/`

优先级 P2：

- `platform-ui/src/api/system/`
- `platform-ui/src/views/system/`
- `platform-core/platform-admin/src/main/java/com/example/platform/admin/`

# 不建议立即重构的模块清单

- 简单 Entity、DTO、VO：多数结构可由字段名表达，立即补注释容易产生噪音。
- 简单 getter、setter、转换方法：禁止补复述型注释。
- 已稳定且无复杂边界的普通 CRUD 方法：应等业务规则稳定后再补关键注释。
- 历史 PRD、非当前 Feature 文档：本阶段不做大规模文档重排。
- Flyway V1/V2 SQL：当前数据库设计已确认，除非后续 SQL 变复杂，否则不补低价值注释。

# 后续实施顺序

1. P0 framework/security/authz/audit/datascope/dynamic-route 注释试点。
2. 复查试点注释是否解释了边界、风险、Deferred 和扩展点。
3. P1 系统管理核心模块按用户、角色、菜单、部门、岗位分批治理。
4. P1 MyBatis XML 只补数据权限、逻辑外键、树结构、聚合查询等非显然 SQL 注释。
5. P1 前端 router、permissions、动态路由注释治理。
6. P2 API client 和页面注释治理，只补契约差异、状态边界和 Deferred。
7. 建立轻量检查规则，检索孤立 `TODO`、英文大段注释和复述型注释。

# 验证计划

- 检查 `specs/004-code-comment-governance/` 是否包含四个 Markdown 文件。
- 检查 `progress.md` 是否遵守 Documentation Governance 标准结构。
- 检查本次 diff 是否只新增 `004` 文档。
- 运行 `scripts/check-spec-progress.sh`。
- 运行 `git diff --check`。

# Deferred（暂缓事项）

- 暂不批量修改代码注释。
- 暂不建立自动化注释 lint。
- 暂不把注释规范写入 Checkstyle 或 ESLint。
- 暂不清理历史英文注释，后续按模块分批处理。

# 风险与技术债

- 风险：如果后续直接批量补注释，可能引入大量低价值注释。处理方向是先 P0 试点，再分批推进。
- 风险：注释规范如果不进入 review checklist，后续仍可能回到 AI 流水注释。处理方向是后续同步到 code review 清单。
- 技术债：当前只能通过人工 review 保证注释质量。处理方向是后续增加轻量脚本检查孤立 TODO 和明显无意义注释。

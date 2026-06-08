# Code Comment Governance Tasks

## Phase 1：规范设计

- [x] 创建 `specs/004-code-comment-governance/`。
- [x] 创建 `spec.md`。
- [x] 创建 `plan.md`。
- [x] 创建 `tasks.md`。
- [x] 创建 `progress.md`。
- [x] 定义 Java 类注释规范。
- [x] 定义 Java 方法注释规范。
- [x] 定义 framework 边界注释规范。
- [x] 定义 Deferred 注释规范。
- [x] 定义风险/限制说明注释规范。
- [x] 定义 MyBatis XML SQL 注释规范。
- [x] 定义 Vue 页面/组合逻辑注释规范。
- [x] 定义 TypeScript API client 注释规范。
- [x] 定义 TODO 注释替代规范。
- [x] 定义禁止无意义注释规范。

## Phase 2：实施准备

- [x] 列出需要优先重构注释的模块清单。
- [x] 列出不建议立即重构的模块清单。
- [x] 定义后续实施顺序。
- [x] 明确当前阶段不修改 Java/Vue/SQL/XML 代码。

## Phase 3：后续实施任务

- [ ] P0 试点重构 framework/security/authz/audit/datascope/dynamic-route 注释。
- [ ] P1 分批治理系统管理核心模块注释。
- [ ] P1 分批治理 MyBatis XML 非显然 SQL 注释。
- [ ] P1 分批治理前端 router、permissions 和动态路由注释。
- [ ] P2 分批治理 TypeScript API client 和 Vue 页面注释。
- [ ] 建立孤立 `TODO`、英文大段注释、无意义注释的轻量检查脚本。
- [ ] 将注释治理规则同步到 code review checklist。

## 验证

- [x] 检查 `004` 目录只包含四个 Markdown 文件。
- [x] 检查 `progress.md` 使用 Documentation Governance 标准结构。
- [x] 运行 `scripts/check-spec-progress.sh`。
- [x] 运行 `git diff --check`。
- [x] 确认未修改 Java、Vue、SQL、XML 文件。

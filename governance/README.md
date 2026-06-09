# 工程治理入口

`governance/` 是本项目的强制工程规范入口，不是参考文档、建议文档或可选最佳实践。

所有 Agent 和开发者在开始任何开发、修复、重构、文档治理、测试补充或提交前，必须先读取本目录，并结合 `AGENTS.md`、PRD、`docs/code-style.md`、对应 `specs/<number>-<feature>/progress.md` 执行。

## 适用范围

- Java 后端、Vue 前端、数据库脚本、权限点、接口、页面、测试和文档治理。
- 新增模块、新增表、新增接口、新增页面、新增权限点。
- 用户管理、角色管理、权限管理、审计日志和业务系统扩展。
- Agent 生成、修改、验证、提交代码的全过程。

## 强制读取顺序

每次开发前必须按以下顺序读取：

1. `AGENTS.md`
2. PRD 或当前 Feature 的 `spec.md`
3. 当前 Feature 的 `progress.md`
4. `docs/code-style.md`
5. `governance/README.md`
6. 本目录下与任务相关的治理文件

若缺少对应 `progress.md`，必须先按 Documentation Governance 创建初始进度文档，再继续开发。

## 强制治理文件

- `comments/code-comment-policy.md`：代码注释治理规范。
- `frontend/frontend-governance.md`：后台管理前端治理规范。
- `review/pr-checklist.md`：提交前与 PR Review 检查清单。
- `testing/governance-checks.md`：治理验证命令和最低检查要求。

## Governance Review

每次开发结束后、提交前必须执行 Governance Review。Review 至少确认：

- 修改是否严格服务 PRD 和当前 Feature。
- 是否遵守最小有效修改原则。
- 是否误提交构建产物、临时上下文、依赖目录或无关文件。
- 后端是否通过格式、Checkstyle 和测试。
- 前端是否通过格式、lint 和构建。
- `progress.md` 是否更新为结构化中文状态，而不是流水日志。
- Deferred、scaffold、技术债和已实现能力是否区分清楚。

未完成 Governance Review 的变更不得提交。

# Documentation Governance Tasks

## Phase 1：建立治理规范

- [x] 创建 `specs/000-documentation-governance/`。
- [x] 创建 `spec.md`。
- [x] 创建 `plan.md`。
- [x] 创建 `tasks.md`。
- [x] 创建 `progress.md`。
- [x] 定义 Spec 四件套职责。
- [x] 定义 `progress.md` 标准结构。
- [x] 定义中文主导语言规范。
- [x] 定义架构决策记录规范。
- [x] 定义 Deferred 记录规范。
- [x] 定义风险与技术债记录规范。

## Phase 2：重构已有 progress.md

- [x] 重构 `specs/001-system-database-design/progress.md`。
- [x] 重构 `specs/002-system-user-role-menu-org/progress.md`。
- [x] 删除低价值流水日志。
- [x] 删除无意义 Agent 执行痕迹。
- [x] 保留关键架构决策、风险、Deferred、阶段成果和下一阶段计划。

## Phase 3：建立后续强制规范

- [x] 更新 `AGENTS.md`，加入 Documentation Governance 强制规则。
- [x] 明确后续 Feature 必须使用统一 `progress.md` 结构。
- [x] 明确业务实现状态必须区分已实现、scaffold、Deferred 和技术债。

## 验证

- [x] 运行 `scripts/check-spec-progress.sh`。
- [x] 运行 `git diff --check`。
- [x] 检查 `000` 目录只包含四个 Markdown 文件。
- [x] 检查 `001`、`002` 的 `progress.md` 标题结构一致。

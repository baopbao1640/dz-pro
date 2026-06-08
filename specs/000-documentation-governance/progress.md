# 当前阶段

Documentation Governance Refactor 已完成文档治理设计与既有进度文档重构。

# 当前目标

建立统一的 Spec / Progress / Phase 文档治理能力，降低后续长期演进和多 Agent 协作失控风险。

# 当前完成情况

- 已创建 `specs/000-documentation-governance/` 四件套。
- 已定义 `spec.md`、`plan.md`、`tasks.md`、`progress.md` 的职责边界。
- 已定义所有 `progress.md` 的统一标题结构。
- 已明确中文主导、英文仅用于技术名词和代码标识。
- 已明确架构决策、Deferred、风险与技术债的记录要求。
- 已将后续强制规范写入 `AGENTS.md`。

# 当前架构决策

- 使用 `000` 编号承载文档治理：文档治理是项目级规则，应优先于业务 Feature。
- `progress.md` 不再作为流水日志：长期演进更需要阶段状态、决策、风险和下一步计划。
- 保留 `spec.md` 与 `plan.md` 的业务设计职责：避免把进度、决策和执行记录混入需求说明。
- Deferred 独立成章：避免后续误把 scaffold 或暂缓事项当作已完成能力。

# 已实现能力

- 平台级文档治理规范。
- `progress.md` 标准结构。
- Deferred 记录机制。
- 风险与技术债记录机制。
- 多 Agent 协作备注机制。
- 后续 Feature 强制遵守入口。

# Deferred（暂缓事项）

- 暂不开发自动化 Markdown lint 规则。
- 暂不批量重写历史 `spec.md` 与 `plan.md`。
- 暂不建立独立 ADR 目录；当前先在 `progress.md` 记录阶段架构决策。

# 风险与技术债

- 风险：后续 Agent 仍可能追加流水日志。影响范围是所有长期 Feature 的进度文档。处理方向是在 review 中强制检查标题结构和内容质量。
- 风险：当前没有自动化检查标题结构。影响范围是 CI 无法完全阻止不合规文档。处理方向是后续增加轻量脚本或 Markdown lint。
- 技术债：已有业务 Spec 文档仍存在英文内容。当前接受原因是本轮只治理 progress 与规则入口。后续可按 Feature 逐步中文化。

# 下一阶段计划

- 后续新 Feature 创建时，先复制本规范定义的 `progress.md` 标准结构。
- 每个阶段收尾时，只更新结构化状态，不追加流水日志。
- 当 Phase 3A 继续推进前，先基于重构后的 `002` progress 重新确认 Deferred、风险和下一步计划。

# 验证结果

- `scripts/check-spec-progress.sh` 已通过。当前 Windows `bash` 默认指向不可用 WSL，因此使用 Git Bash login shell 执行：`C:\Program Files\Git\bin\bash.exe -lc 'cd /f/AIworkspance/content && scripts/check-spec-progress.sh'`。
- `git diff --check` 已通过，仅出现 CRLF 提示，无 whitespace error。
- `000` 目录已确认只包含 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。
- `000`、`001`、`002` 的 `progress.md` 已确认使用统一一级标题结构。
- 已检查重构后的 `progress.md` 不再包含旧式英文流水日志小标题。

# Agent 协作备注

多 Agent 协作时，只记录职责边界、共享结构变更、冲突处理和同步要求；不记录低价值执行过程。

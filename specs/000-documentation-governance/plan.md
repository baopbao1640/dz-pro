# Documentation Governance Plan

## 阶段边界

当前只做文档治理设计和既有 `progress.md` 重构。

禁止改动：

- Java 业务代码。
- Vue 页面代码。
- Mapper、Service、Controller、Entity、XML。
- Flyway migration。
- OpenAPI 实现。
- 新业务功能。

## 实施计划

### Phase 1：建立治理规范

- 创建 `specs/000-documentation-governance/`。
- 编写 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。
- 明确四件套职责、`progress.md` 标准结构、语言规范、架构决策、Deferred、技术债和 Agent 协作规则。

### Phase 2：重构已有进度文档

重构以下文件：

- `specs/001-system-database-design/progress.md`
- `specs/002-system-user-role-menu-org/progress.md`

重构目标：

- 中文化。
- 阶段化。
- 架构化。
- 决策化。
- Deferred 化。
- 技术债化。

删除内容：

- 低价值流水日志。
- 无意义 Agent 执行痕迹。
- 重复验证记录。
- 英文主导内容。

保留内容：

- 关键阶段成果。
- 架构决策。
- 已实现能力。
- Deferred。
- 风险与技术债。
- 下一阶段计划。
- 有代表性的验证结果。

### Phase 3：固化后续强制规范

- 更新 `AGENTS.md`，要求后续 Feature 遵守 Documentation Governance。
- 明确 `progress.md` 不是执行日志，而是阶段状态、决策、风险和计划的治理文档。

## 架构决策

- 将文档治理作为 `000` 号 Spec：原因是它属于项目级规则，应先于业务 Feature 编号。
- 不修改 `spec.md` 和 `plan.md` 的历史业务内容：原因是本次重点治理长期进度文档，不做历史设计大重排。
- 不引入自动化重写脚本：原因是当前文档数量少，人工重构更容易保留语义并避免误删关键决策。
- 在 `AGENTS.md` 固化规则：原因是后续多 Agent 协作必须有统一入口，不能只依赖口头约定。

## 验证计划

- 检查 `specs/000-documentation-governance/` 是否包含四个 Markdown 文件。
- 检查 `001`、`002` 的 `progress.md` 是否都采用统一标题结构。
- 检查重构后的 `progress.md` 是否中文主导。
- 运行 `scripts/check-spec-progress.sh`。
- 运行 `git diff --check`。

## 不做事项

- 不继续 Phase 3A 业务功能开发。
- 不修复现有业务代码风险。
- 不创建新的 Flyway SQL。
- 不新增 Java/Vue/XML/OpenAPI 实现。
- 不提交 git commit。

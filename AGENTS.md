# AGENTS.md

## 1. PRD 优先原则

所有开发必须以 PRD 为最高依据，避免任意发挥。

## 2. 最小有效修改原则

只改 PRD 要求的模块，严禁无差别重构。

## 3. 不提交未验证代码原则

所有修改必须通过编译/基础验证后才能提交。

## 4. 公共模块引用原则

子模块需通过父工程统一管理依赖版本。

## 5. 编程规范执行原则

新增和修改代码必须遵守 `docs/code-style.md`，并通过后端格式检查、Checkstyle、测试，以及前端 lint、格式检查、构建验证。

## 6. Spec Kit 功能流程原则

新增模块、新增表、新增接口、新增页面、新增权限点，以及用户管理、角色管理、权限管理、审计日志、业务系统扩展，必须走 Spec Kit 流程，并在 `specs/<number>-<feature>/` 下维护 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。

## 7. 进度钩子原则

每次开始某个 feature 或 task 前，必须先读取对应 `progress.md`。若不存在，必须先创建初始进度文档。每完成一个 task 或 feature 后，必须更新 `progress.md`，记录已完成内容、验证结果、下一步计划和风险。

## 8. Documentation Governance 原则

所有 `specs/<number>-<feature>/` 必须遵守 `specs/000-documentation-governance/`。`progress.md` 必须使用统一中文结构，明确当前阶段、当前目标、当前完成情况、当前架构决策、已实现能力、Deferred（暂缓事项）、风险与技术债、下一阶段计划、验证结果和 Agent 协作备注。

禁止把 `progress.md` 写成英文流水日志或低价值 Agent 执行记录。所有能力状态必须明确区分：已实现、scaffold、intentionally deferred、技术债和未来扩展点。

<!-- SPECKIT START -->
For additional context about technologies to be used, project structure,
shell commands, and other important information, read the current plan
<!-- SPECKIT END -->
